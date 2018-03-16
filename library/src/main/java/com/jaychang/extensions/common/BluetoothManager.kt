package com.jaychang.extensions.common

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothDevice.*
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothProfile.STATE_CONNECTED
import android.bluetooth.BluetoothProfile.STATE_DISCONNECTED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import java.util.*

@SuppressLint("StaticFieldLeak", "MissingPermission")
object BluetoothManager {
  private val TAG = BluetoothManager.javaClass.name
  private lateinit var appContext: Context
  private lateinit var bluetoothAdapter: BluetoothAdapter
  private var bluetoothGatt: BluetoothGatt? = null
  private var deviceMacAddress: String? = null
  private var isServiceFound: Boolean = false
  private var stopReconnect: Boolean = false
  private val transactionQueue = LinkedList<TransactionQueueItem>()
  private var isTxQueueProcessing = false
  private var deviceName = ""

  val isBluetoothOn: Boolean
    get() {
      return bluetoothAdapter.isEnabled
    }

  val onBluetoothOn: (() -> Unit)? = null
  val onBluetoothOff: (() -> Unit)? = null
  val onSearchingDone: (() -> Unit)? = null
  val onClassicConnected: ((device: BluetoothDevice) -> Unit)? = null
  val onClassicAboutToDisconnect: ((device: BluetoothDevice) -> Unit)? = null
  val onClassicDisconnected: ((device: BluetoothDevice) -> Unit)? = null
  val onServicesDiscovered: (() -> Unit)? = null
  val onDeviceFound: ((device: BluetoothDevice) -> Unit)? = null

  private val gattCallback = object : BluetoothGattCallback() {
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
      super.onConnectionStateChange(gatt, status, newState)
      Log.d(TAG, "onConnectionStateChange received: " + status)

      if (status == GATT_SUCCESS) {
        if (newState == STATE_CONNECTED) {
          Log.d(TAG, "connection state change to Connected, try to discover services")
          bluetoothGatt?.discoverServices()
        } else if (newState == STATE_DISCONNECTED) {
          Log.d(TAG, "connection state change to Disconnected, close ble connection")
          disconnectBle()
        }
      } else {
        reconnect()
      }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
      super.onServicesDiscovered(gatt, status)
      Log.d(TAG, "service discovered, status: " + status)
      if (status != GATT_SUCCESS) {
        reconnect()
      } else {
        isServiceFound = true
        onServicesDiscovered?.invoke()
      }
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
      super.onCharacteristicRead(gatt, characteristic, status)
      Log.d(TAG, "onCharacteristicRead, status: " + status)
      if (status != GATT_SUCCESS) {
        reconnect()
      }
    }

    override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
      super.onCharacteristicWrite(gatt, characteristic, status)
      Log.d(TAG, "onCharacteristicWrite, status: " + status)
      if (status != GATT_SUCCESS) {
        reconnect()
      } else {
        processTransactionQueue()
      }
    }
  }

  fun connectBle(bluetoothDeviceAddress: String?): Boolean {
    stopReconnect = false

    if (bluetoothDeviceAddress == null) {
      Log.d(TAG, "Unspecified address.")
      return false
    }

    if (bluetoothGatt != null) {
      Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.")
      return bluetoothGatt!!.connect()
    }

    val device = bluetoothAdapter.getRemoteDevice(bluetoothDeviceAddress)
    if (device == null) {
      Log.d(TAG, "Device not found. Unable to connectBle.")
      return false
    }

    bluetoothGatt = if (Build.VERSION.SDK_INT >= 23) {
      device.connectGatt(appContext, false, gattCallback, TRANSPORT_LE)
    } else {
      device.connectGatt(appContext, false, gattCallback)
    }

    Log.d(TAG, "Trying to create a new connection")

    return true
  }

  fun disconnectBle() {
    bluetoothGatt?.let {
      it.disconnect()
      it.close()
    }
    bluetoothGatt = null
    isServiceFound = false
    stopReconnect = true
    Log.d(TAG, "bluetoothGatt closed")
  }

  private fun reconnect() {
    if (stopReconnect) {
      Log.d(TAG, "Stop reconnect.")
      return
    }

    Log.d(TAG, "reconnect")
    disconnectBle()
    connectBle(deviceMacAddress)
  }

  private fun writeCharacteristic(serviceUUID: UUID, characteristicUUID: UUID, data: ByteArray): Boolean {
    if (bluetoothGatt == null) {
      return false
    }

    val service = bluetoothGatt!!.getService(serviceUUID)

    if (service == null) {
      Log.d(TAG, "No service")
      return false
    }

    val characteristic = service.getCharacteristic(characteristicUUID)

    if (characteristic == null) {
      Log.d(TAG, "No characteristic")
      return false
    }

    characteristic.value = data

    return bluetoothGatt!!.writeCharacteristic(characteristic)
  }

  private fun readCharacteristic(serviceUUID: UUID, characteristicUUID: UUID): Boolean {
    if (bluetoothGatt == null) {
      return false
    }

    val service = bluetoothGatt!!.getService(serviceUUID)
    val characteristic = service.getCharacteristic(characteristicUUID)
    return bluetoothGatt!!.readCharacteristic(characteristic)
  }

  private fun registerBluetoothOnOffStateChange() {
    Log.d(TAG, "Register bluetooth on / off state change")
    appContext.registerReceiver(object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
        if (state == BluetoothAdapter.STATE_ON) {
          onBluetoothOn?.invoke()
          Log.d(TAG, "Bluetooth is turn on")
        } else if (state == BluetoothAdapter.STATE_OFF) {
          onBluetoothOff?.invoke()
          Log.d(TAG, "Bluetooth is turn off")
        }
      }
    }, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
  }

  /**
   * - ACTION_CONNECTION_STATE_CHANGED is not working in Android 7, so we should use ACTION_ACL_CONNECTED and ACTION_ACL_DISCONNECTED .
   * - ACTION_ACL_DISCONNECTED will be call when app lost focus(e.g. back to home screen, permission dialog shows up, etc), so we need to
   * check if the profile is really off.
   */
  private fun registerDeviceClassicStateChange() {
    Log.d(TAG, "Register bluetooth device classic state change")

    val receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (!isMyDevicePaired()) {
          return
        }

        val device = intent.getParcelableExtra<BluetoothDevice>(EXTRA_DEVICE)
        deviceMacAddress = device.address

        if (deviceName != device.name) {
          return
        }

        when (intent.action) {
          ACTION_FOUND -> {
            onDeviceFound?.invoke(device)
            Log.d(TAG, "Device ${device.name} is found")
          }
          ACTION_ACL_CONNECTED -> {
            onClassicConnected?.invoke(device)
            Log.d(TAG, "Device ${device.name} is classic connected")
          }
          ACTION_ACL_DISCONNECT_REQUESTED -> {
            onClassicAboutToDisconnect?.invoke(device)
            Log.d(TAG, "Device ${device.name} is classic about to disconnect")
          }
          ACTION_ACL_DISCONNECTED -> {
            onClassicDisconnected?.invoke(device)
            Log.d(TAG, "Device ${device.name} is classic disconnected")
          }
          ACTION_DISCOVERY_FINISHED -> {
            onSearchingDone?.invoke()
            Log.d(TAG, "Searching device is done")
          }
        }
      }
    }

    appContext.registerReceiver(receiver, IntentFilter(ACTION_ACL_CONNECTED))
    appContext.registerReceiver(receiver, IntentFilter(ACTION_ACL_DISCONNECT_REQUESTED))
    appContext.registerReceiver(receiver, IntentFilter(ACTION_ACL_DISCONNECTED))
  }

  fun init(context: Context, deviceName: String) {
    this.deviceName = deviceName
    appContext = context.applicationContext
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    registerBluetoothOnOffStateChange()
    registerDeviceClassicStateChange()
  }

  private fun isMyDevicePaired(): Boolean {
    return bluetoothAdapter.bondedDevices.any { deviceName == it.name }
  }

  private fun addToTransactionQueue(transactionQueueItem: TransactionQueueItem) {
    transactionQueue.add(transactionQueueItem)

    // If there is no other transmission processing, go do this one!
    if (!isTxQueueProcessing) {
      processTransactionQueue()
    }
  }

  private fun processTransactionQueue() {
    if (transactionQueue.size <= 0) {
      isTxQueueProcessing = false
      return
    }

    isTxQueueProcessing = true
    val txQueueItem = transactionQueue.remove()
    when (txQueueItem.type) {
      BluetoothManager.QueueItemType.WriteCharacteristic -> writeCharacteristic(txQueueItem.serviceUUID, txQueueItem.characteristicUUID, txQueueItem.dataToWrite!!)
      BluetoothManager.QueueItemType.ReadCharacteristic -> readCharacteristic(txQueueItem.serviceUUID, txQueueItem.characteristicUUID)
    }
  }

  /**
   * Callback only be called when bluetooth is on
   */
  fun getConnectedDevices(bluetoothProfile: Int, callback: (List<BluetoothDevice>) -> Unit) {
    val listener = object : BluetoothProfile.ServiceListener {
      override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
        if (profile == bluetoothProfile) {
          callback.invoke(proxy.connectedDevices)
          bluetoothAdapter.closeProfileProxy(bluetoothProfile, proxy)
        } else {
          callback.invoke(listOf())
        }
      }

      override fun onServiceDisconnected(profile: Int) {}
    }
    bluetoothAdapter.getProfileProxy(appContext, listener, BluetoothProfile.A2DP)
  }

  fun enqueueWriteDataToCharacteristic(serviceUUID: UUID, characteristicUUID: UUID, dataToWrite: ByteArray) {
    val transactionQueueItem = TransactionQueueItem(serviceUUID = serviceUUID, characteristicUUID = characteristicUUID, dataToWrite = dataToWrite, type = QueueItemType.WriteCharacteristic)
    addToTransactionQueue(transactionQueueItem)
  }

  fun enqueueReadCharacteristicValue(serviceUUID: UUID, characteristicUUID: UUID) {
    val transactionQueueItem = TransactionQueueItem(serviceUUID = serviceUUID, characteristicUUID = characteristicUUID, type = QueueItemType.ReadCharacteristic)
    addToTransactionQueue(transactionQueueItem)
  }

  /**
   * Transaction Queue
   */
  private class TransactionQueueItem(val serviceUUID: UUID, val characteristicUUID: UUID, val dataToWrite: ByteArray? = null, val type: QueueItemType)

  private enum class QueueItemType {
    ReadCharacteristic,
    WriteCharacteristic
  }
}
