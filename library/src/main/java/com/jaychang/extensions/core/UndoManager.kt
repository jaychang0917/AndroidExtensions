package com.jaychang.extensions.core

import android.support.annotation.CallSuper
import java.util.*

object UndoManager {
  val undoStack = Stack<Operation>()
  val redoStack = Stack<Operation>()

  val isUndoAvailable: Boolean
    get() = undoStack.size > 0

  val isRedoAvailable: Boolean
    get() = redoStack.size > 0

  fun undo() {
    if (isUndoAvailable) {
      val operation = undoStack.pop()
      operation.executeUndo()
    }
  }

  fun redo() {
    if (isRedoAvailable) {
      val operation = redoStack.pop()
      operation.execute()
    }
  }

  fun clearRedoTasks() {
    redoStack.clear()
  }

  fun clearUndoTasks() {
    undoStack.clear()
  }

  fun addUndoTask(operation: Operation) {
    undoStack.add(operation)
  }

  fun addRedoTask(operation: Operation) {
    redoStack.add(operation)
  }

  class Operation {
    @CallSuper
    fun executeUndo() {
      addRedoTask(this)
    }

    @CallSuper
    fun execute() {
      addUndoTask(this)
    }
  }

}