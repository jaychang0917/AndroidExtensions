package com.jaychang.extensions.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaychang.extensions.R;

public class ToolbarEx extends RelativeLayout {
  private final int WHITE = ContextCompat.getColor(getContext(), android.R.color.white);

  private ViewStub leftIconViewStub;
  private ViewStub leftTextViewStub;
  private ViewStub titleTextViewStub;
  private ViewStub titleIconViewStub;
  private ViewStub rightTextViewStub;
  private ViewStub rightIconViewStub;
  private ViewStub rightIcon2ViewStub;
  private ViewStub rightIcon3ViewStub;
  private ViewStub rightIcon4ViewStub;
  private ViewStub rightIcon5ViewStub;
  private ImageButton leftIconView;
  private TextView leftTextView;
  private TextView titleTextView;
  private ImageView titleIconView;
  private TextView rightTextView;
  private ImageButton rightIconView;
  private ImageButton rightIcon2View;
  private ImageButton rightIcon3View;
  private ImageButton rightIcon4View;
  private ImageButton rightIcon5View;
  private View rootView;

  private int leftIcon;
  private CharSequence leftText;
  private int leftTextSize;
  private int leftTextStyle;
  private String leftTextFont;
  private int leftTextIcon;
  private int leftTextIconPosition;
  private ColorStateList leftTextColor;

  private CharSequence title;
  private int titleTextSize;
  private int titleTextStyle;
  private String titleTextFont;
  private int titleTextIcon;
  private int titleIcon;
  private int titleTextIconPosition;
  private int titleTextGravity;
  private ColorStateList titleTextColor;
  private boolean isTitleSingleLine;

  private int rightIcon;
  private int rightIcon2;
  private int rightIcon3;
  private int rightIcon4;
  private int rightIcon5;
  private CharSequence rightText;
  private int rightTextSize;
  private int rightTextIcon;
  private int rightTextIconPosition;
  private int rightTextStyle;
  private String rightTextFont;
  private ColorStateList rightTextColor;

  private ColorStateList toolbarTitleTextColor;
  private int toolbarTitleTextSize;
  private int toolbarTitleTextStyle;
  private String toolbarTitleTextFont;
  private int toolbarTitleTextGravity;
  private ColorStateList toolbarTextColor;
  private int toolbarTextSize;
  private int toolbarTextStyle;
  private String toolbarTextFont;
  private int toolbarBackgroundColor;
  private int toolbarHeight;
  private boolean toolbarTitleSingleLine;
  private boolean isTitleComponentInitialized = true;
  private boolean isTitleIconComponentInitialized = true;
  private boolean isLeftComponentTextInitialized = true;
  private boolean isRightComponentTextInitialized = true;

  private Drawable backgroundDrawable;

  public ToolbarEx(Context context) {
    this(context, null);
  }

  public ToolbarEx(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ToolbarEx(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflateViews(context);
    initAttrs(context, attrs, defStyleAttr);
    initViews();
  }

  private void inflateViews(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.toolbar_toolbar, this);
    leftIconViewStub = view.findViewById(R.id.leftIconStub);
    leftTextViewStub = view.findViewById(R.id.leftTextStub);
    titleTextViewStub = view.findViewById(R.id.titleStub);
    titleIconViewStub = view.findViewById(R.id.titleIconStub);
    rightIconViewStub = view.findViewById(R.id.rightIconStub);
    rightIcon2ViewStub = view.findViewById(R.id.rightIcon2Stub);
    rightIcon3ViewStub = view.findViewById(R.id.rightIcon3Stub);
    rightIcon4ViewStub = view.findViewById(R.id.rightIcon4Stub);
    rightIcon5ViewStub = view.findViewById(R.id.rightIcon5Stub);
    rightTextViewStub = view.findViewById(R.id.rightTextStub);
    rootView = view.findViewById(R.id.rootView);
  }

  private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
    final int PIXEL_SIZE_17 = sp2px(getContext(), 17);
    final int PIXEL_SIZE_20 = sp2px(getContext(), 20);

    TypedArray typedArray = context.getTheme()
      .obtainStyledAttributes(attrs, R.styleable.ToolbarEx, defStyleAttr, 0);

    leftIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_leftIcon, 0);
    leftText = typedArray.getString(R.styleable.ToolbarEx_toolbar_leftText);
    leftTextSize = typedArray.getDimensionPixelSize(R.styleable.ToolbarEx_toolbar_leftTextSize, PIXEL_SIZE_17);
    leftTextIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_leftTextIcon, 0);
    leftTextIconPosition = typedArray.getInt(R.styleable.ToolbarEx_toolbar_leftTextIconPosition, 0);
    leftTextColor = typedArray.getColorStateList(R.styleable.ToolbarEx_toolbar_leftTextColor);
    leftTextStyle = typedArray.getInt(R.styleable.ToolbarEx_toolbar_leftTextStyle, 0);
    leftTextFont = typedArray.getString(R.styleable.ToolbarEx_toolbar_leftTextFont);

    title = typedArray.getString(R.styleable.ToolbarEx_toolbar_title);
    titleIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_titleIcon, 0);
    titleTextSize = typedArray.getDimensionPixelSize(R.styleable.ToolbarEx_toolbar_titleTextSize, PIXEL_SIZE_20);
    titleTextIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_titleTextIcon, 0);
    titleTextIconPosition = typedArray.getInt(R.styleable.ToolbarEx_toolbar_titleTextIconPosition, 0);
    titleTextColor = typedArray.getColorStateList(R.styleable.ToolbarEx_toolbar_titleTextColor);
    isTitleSingleLine = typedArray.getBoolean(R.styleable.ToolbarEx_toolbar_titleSingleLine, true);
    titleTextGravity = typedArray.getInt(R.styleable.ToolbarEx_toolbar_titleTextGravity, -1);
    titleTextStyle = typedArray.getInt(R.styleable.ToolbarEx_toolbar_titleTextStyle, 0);
    titleTextFont = typedArray.getString(R.styleable.ToolbarEx_toolbar_titleTextFont);

    rightIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightIcon, 0);
    rightIcon2 = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightIcon2, 0);
    rightIcon3 = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightIcon3, 0);
    rightIcon4 = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightIcon4, 0);
    rightIcon5 = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightIcon5, 0);
    rightText = typedArray.getString(R.styleable.ToolbarEx_toolbar_rightText);
    rightTextSize = typedArray.getDimensionPixelSize(R.styleable.ToolbarEx_toolbar_rightTextSize, PIXEL_SIZE_17);
    rightTextIcon = typedArray.getResourceId(R.styleable.ToolbarEx_toolbar_rightTextIcon, 0);
    rightTextIconPosition = typedArray.getInt(R.styleable.ToolbarEx_toolbar_rightTextIconPosition, 0);
    rightTextColor = typedArray.getColorStateList(R.styleable.ToolbarEx_toolbar_rightTextColor);
    rightTextStyle = typedArray.getInt(R.styleable.ToolbarEx_toolbar_rightTextStyle, 0);
    rightTextFont = typedArray.getString(R.styleable.ToolbarEx_toolbar_rightTextFont);

    typedArray.recycle();

    // background attr
    int[] backgroundAttr = new int[] {
      android.R.attr.background
    };
    TypedArray bgTypeArray = context.obtainStyledAttributes(attrs, backgroundAttr);
    backgroundDrawable = bgTypeArray.getDrawable(0);
    bgTypeArray.recycle();

    // global attr
    int[] globalStyleAttrs = new int[]{
      R.attr.toolbarTitleTextColor,
      R.attr.toolbarTitleTextSize,
      R.attr.toolbarTitleTextStyle,
      R.attr.toolbarTitleTextGravity,
      R.attr.toolbarTextColor,
      R.attr.toolbarTextSize,
      R.attr.toolbarTextStyle,
      R.attr.toolbarBackgroundColor,
      R.attr.toolbarHeight,
      R.attr.toolbarTitleSingleLine,
      R.attr.toolbarTitleTextFont,
      R.attr.toolbarTextFont
    };
    TypedArray ta = context.obtainStyledAttributes(globalStyleAttrs);
    toolbarTitleTextColor = ta.getColorStateList(0);
    toolbarTitleTextSize = ta.getDimensionPixelSize(1, PIXEL_SIZE_20);
    toolbarTitleTextStyle = ta.getInt(2, 0);
    toolbarTitleTextGravity = ta.getInt(3, -1);
    toolbarTextColor = ta.getColorStateList(4);
    toolbarTextSize = ta.getDimensionPixelSize(5, PIXEL_SIZE_17);
    toolbarTextStyle = ta.getInt(6, 0);
    toolbarBackgroundColor = ta.getResourceId(7, 0);
    toolbarHeight = ta.getDimensionPixelSize(8, 0);
    toolbarTitleSingleLine = ta.getBoolean(9, false);
    toolbarTitleTextFont = ta.getString(10);
    toolbarTextFont = ta.getString(11);
    ta.recycle();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (toolbarHeight != 0) {
      rootView.getLayoutParams().height = toolbarHeight;
    }
  }

  private void initViews() {
    initLeftIcon();
    initLeftText();
    initTitle();
    initTitleIcon();
    initRightIcon();
    initRightText();
    initBackground();
  }

  private void initLeftIcon() {
    if (leftIcon != 0) {
      setLeftIcon(leftIcon);
    }
  }

  private void initLeftText() {
    if (!TextUtils.isEmpty(leftText)) {
      setLeftText(leftText);
      initLeftTextComponent();
    } else {
      isLeftComponentTextInitialized = false;
    }
  }

  private void initLeftTextComponent() {
    if (leftTextIcon != 0) {
      setLeftTextIcon(leftTextIcon);
    }

    if (leftTextColor != null) {
      setLeftTextColor(leftTextColor);
    } else if (toolbarTextColor != null) {
      setLeftTextColor(toolbarTextColor);
    } else {
      setLeftTextColor(WHITE);
    }


    setLeftTextStyle(toolbarTextFont, toolbarTextStyle);
    if (!TextUtils.isEmpty(leftTextFont) && leftTextStyle == 0) {
      setLeftTextStyle(leftTextFont, toolbarTextStyle != 0? toolbarTextStyle: Typeface.NORMAL);
    } else if (!TextUtils.isEmpty(leftTextFont) && leftTextStyle != 0) {
      setLeftTextStyle(leftTextFont, leftTextStyle);
    } else if (TextUtils.isEmpty(leftTextFont) && leftTextStyle != 0) {
      if (!TextUtils.isEmpty(toolbarTextFont)) {
        setLeftTextStyle(toolbarTextFont, leftTextStyle);
      } else {
        setLeftTextStyle(leftTextStyle);
      }
    }

    setLeftTextSize(leftTextSize);
    if (toolbarTextSize != 0) {
      setLeftTextSize(toolbarTextSize);
    }

    isLeftComponentTextInitialized = true;
  }

  private void initTitle() {
    if (!TextUtils.isEmpty(title)) {
      setTitle(title);
      initTitleComponent();
    } else {
      isTitleComponentInitialized = false;
    }
  }

  private void initTitleComponent() {
    titleTextView.setClickable(false);

    if (titleTextIcon != 0) {
      setTitleTextIcon(titleTextIcon);
    }

    if (titleTextColor != null) {
      setTitleTextColor(titleTextColor);
    } else if (toolbarTitleTextColor != null) {
      setTitleTextColor(toolbarTitleTextColor);
    } else if (toolbarTextColor != null) {
      setTitleTextColor(toolbarTextColor);
    } else {
      setTitleTextColor(WHITE);
    }

    setTitleTextSize(titleTextSize);
    if (toolbarTitleTextSize != 0) {
      setTitleTextSize(toolbarTitleTextSize);
    }

    setTitleTextTypeface(toolbarTitleTextFont, toolbarTitleTextStyle);
    if (!TextUtils.isEmpty(titleTextFont) && titleTextStyle == 0) {
      setTitleTextTypeface(titleTextFont, toolbarTitleTextStyle != 0? toolbarTitleTextStyle: Typeface.NORMAL);
    } else if (!TextUtils.isEmpty(titleTextFont) && titleTextStyle != 0) {
      setTitleTextTypeface(titleTextFont, titleTextStyle);
    } else if (TextUtils.isEmpty(titleTextFont) && titleTextStyle != 0) {
      if (!TextUtils.isEmpty(toolbarTitleTextFont)) {
        setTitleTextTypeface(toolbarTitleTextFont, titleTextStyle);
      } else {
        setTitleTextTypeface(titleTextStyle);
      }
    }

    if (toolbarTitleTextGravity == 0){
      setTitleLeftMargin();
    } else if (toolbarTitleTextGravity == 1) {
      setTitleLeftMargin();
      alightTitleCenter();
    }

    if (titleTextGravity == 0) {
      setTitleLeftMargin();
    } else if (titleTextGravity == 1) {
      alightTitleCenter();
    } else if (toolbarTitleTextGravity == 0){
      setTitleLeftMargin();
    } else if (toolbarTitleTextGravity == 1) {
      alightTitleCenter();
    } else {
      setTitleLeftMargin();
    }

    if (toolbarTitleSingleLine) {
      setTitleSingleLine(true);
    } else {
      setTitleSingleLine(isTitleSingleLine);
    }

    isTitleComponentInitialized = true;
  }

  private void initTitleIcon() {
    if (titleIcon != 0) {
      setTitleIcon(titleIcon);
      initTitleIconComponent();
    } else {
      isTitleIconComponentInitialized = false;
    }
  }

  private void initTitleIconComponent() {
    alightTitleIconCenter();
    isTitleIconComponentInitialized = true;
  }

  private void initRightIcon() {
    if (rightIcon != 0) {
      setRightIcon(rightIcon);
    }
    if (rightIcon2 != 0) {
      setRightIcon2(rightIcon2);
    }
    if (rightIcon3 != 0) {
      setRightIcon3(rightIcon3);
    }
    if (rightIcon4 != 0) {
      setRightIcon4(rightIcon4);
    }
    if (rightIcon5 != 0) {
      setRightIcon5(rightIcon5);
    }
  }

  private void initRightText() {
    if (!TextUtils.isEmpty(rightText)) {
      setRightText(rightText);
      initRightTextComponent();
    } else {
      isRightComponentTextInitialized = false;
    }
  }

  private void initRightTextComponent() {
    if (rightTextIcon != 0) {
      setRightTextIcon(rightTextIcon);
    }

    if (rightTextColor != null) {
      setRightTextColor(rightTextColor);
    } else if (toolbarTextColor != null) {
      setRightTextColor(toolbarTextColor);
    } else if (toolbarTitleTextColor != null) {
      setRightTextColor(toolbarTitleTextColor);
    } else {
      setRightTextColor(WHITE);
    }

    setRightTextStyle(toolbarTextFont, toolbarTextStyle);
    if (!TextUtils.isEmpty(rightTextFont) && rightTextStyle == 0) {
      setRightTextStyle(rightTextFont, toolbarTextStyle != 0? toolbarTextStyle: Typeface.NORMAL);
    } else if (!TextUtils.isEmpty(rightTextFont) && rightTextStyle != 0) {
      setRightTextStyle(rightTextFont, rightTextStyle);
    } else if (TextUtils.isEmpty(rightTextFont) && rightTextStyle != 0) {
      if (!TextUtils.isEmpty(toolbarTextFont)) {
        setRightTextStyle(toolbarTextFont, rightTextStyle);
      } else {
        setRightTextStyle(rightTextStyle);
      }
    }

    setRightTextSize(rightTextSize);
    if (toolbarTextSize != 0) {
      setRightTextSize(toolbarTextSize);
    }

    isRightComponentTextInitialized = true;
  }

  private void initBackground() {
    if (backgroundDrawable != null) {
      setBackgroundDrawable(backgroundDrawable);
    } else if (toolbarBackgroundColor != 0) {
      setBackgroundResource(toolbarBackgroundColor);
    } else {
      int colorPrimary = getAttrResourceId(getContext(), R.attr.colorPrimary);
      if (colorPrimary != 0) {
        setBackgroundResource(colorPrimary);
      }
    }
  }

  /**
   * title
   */
  public void setTitle(@StringRes int title) {
    setTitle(getResources().getString(title));
  }

  public void setTitle(CharSequence title) {
    inflateTitleViewIfNeed();
    if (!isTitleComponentInitialized) {
      initTitleComponent();
    }
    titleTextView.setText(title);
  }

  public void setTitleIcon(@DrawableRes int titleIcon) {
    inflateTitleIconViewIfNeed();
    if (!isTitleIconComponentInitialized) {
      initTitleIconComponent();
    }
    titleIconView.setBackgroundResource(titleIcon);
  }

  public void setTitleLeftMargin() {
    inflateTitleViewIfNeed();
    LayoutParams params = ((LayoutParams) titleTextView.getLayoutParams());
    params.leftMargin = dp2px(getContext(), 72 - 16);
    titleTextView.setLayoutParams(params);
  }

  public void alightTitleCenter() {
    inflateTitleViewIfNeed();
    LayoutParams params = ((LayoutParams) titleTextView.getLayoutParams());
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    titleTextView.setLayoutParams(params);
  }

  private void alightTitleIconCenter() {
    inflateTitleIconViewIfNeed();
    LayoutParams params = ((LayoutParams) titleIconView.getLayoutParams());
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    titleIconView.setLayoutParams(params);
  }

  public void setTitleTextIcon(@DrawableRes int titleTextIcon) {
    inflateTitleViewIfNeed();
    if (titleTextIconPosition == 0) {
      titleTextView.setCompoundDrawablesWithIntrinsicBounds(titleTextIcon, 0, 0, 0);
    } else {
      titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, titleTextIcon, 0);
    }
  }

  public void setTitleTextColor(@ColorInt int color) {
    inflateTitleViewIfNeed();
    titleTextView.setTextColor(color);
  }

  public void setTitleTextColor(ColorStateList colorStateList) {
    inflateTitleViewIfNeed();
    titleTextView.setTextColor(colorStateList);
  }

  public void setTitleTextTypeface(@Nullable String font, int typeface) {
    inflateTitleViewIfNeed();
    if (font != null) {
      titleTextView.setTypeface(Typeface.create(font, typeface));
    } else {
      titleTextView.setTypeface(null, typeface);
    }
  }

  public void setTitleTextTypeface(int typeface) {
    setTitleTextTypeface(null, typeface);
  }

  public void setTitleTextSize(@Px int textSize) {
    inflateTitleViewIfNeed();
    titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
  }

  public void setTitleOnClickListener(View.OnClickListener listener) {
    inflateTitleViewIfNeed();
    titleTextView.setOnClickListener(listener);
  }

  public void setTitleSingleLine(boolean isSingleLine) {
    titleTextView.setSingleLine(isSingleLine);
    if (isSingleLine) {
      titleTextView.setEllipsize(TextUtils.TruncateAt.END);
    }
  }

  /**
   * left
   */
  public void setLeftText(@StringRes int stringRes) {
    setLeftText(getResources().getString(stringRes));
  }

  public void setLeftText(CharSequence text) {
    inflateLeftTextViewIfNeed();
    if (!isLeftComponentTextInitialized) {
      initLeftTextComponent();
    }
    leftTextView.setText(text);
  }

  public void setLeftTextIcon(@DrawableRes int leftTextIcon) {
    inflateLeftTextViewIfNeed();
    if (leftTextIconPosition == 0) {
      leftTextView.setCompoundDrawablesWithIntrinsicBounds(leftTextIcon, 0, 0, 0);
    } else {
      leftTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, leftTextIcon, 0);
    }
  }

  public void setLeftTextColor(@ColorInt int color) {
    inflateLeftTextViewIfNeed();
    leftTextView.setTextColor(color);
  }

  public void setLeftTextColor(ColorStateList colorStateList) {
    inflateLeftTextViewIfNeed();
    leftTextView.setTextColor(colorStateList);
  }

  public void setLeftTextStyle(@Nullable String font, int style) {
    inflateLeftTextViewIfNeed();
    if (font != null) {
      leftTextView.setTypeface(Typeface.create(font, style));
    } else {
      leftTextView.setTypeface(null, style);
    }
  }

  public void setLeftTextStyle(int style) {
    setLeftTextStyle(null, style);
  }

  public void setLeftTextSize(@Px int textSize) {
    inflateLeftTextViewIfNeed();
    leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
  }

  public void setLeftTextIcon(@DrawableRes int leftTextIcon, boolean isLeft) {
    this.leftTextIconPosition = isLeft ? 0 : 1;
    setLeftTextIcon(leftTextIcon);
  }

  public void setLeftTextOnClickListener(View.OnClickListener listener) {
    inflateLeftTextViewIfNeed();
    leftTextView.setOnClickListener(listener);
  }

  public void setLeftIcon(@DrawableRes int drawableRes) {
    inflateLeftIconViewIfNeed();
    leftIconView.setImageResource(drawableRes);
  }

  public void setLeftIconOnClickListener(View.OnClickListener listener) {
    inflateLeftIconViewIfNeed();
    leftIconView.setOnClickListener(listener);
  }

  /**
   * right
   */
  public void setRightText(@StringRes int stringRes) {
    setRightText(getResources().getString(stringRes));
  }

  public void setRightText(CharSequence text) {
    inflateRightTextViewIfNeed();
    if (!isRightComponentTextInitialized) {
      initRightTextComponent();
    }
    rightTextView.setText(text);
  }

  public void setRightTextIcon(@DrawableRes int rightTextIcon) {
    inflateRightTextViewIfNeed();
    if (rightTextIconPosition == 0) {
      rightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightTextIcon, 0);
    } else {
      rightTextView.setCompoundDrawablesWithIntrinsicBounds(rightTextIcon, 0, 0, 0);
    }
  }

  public void setRightTextIcon(@DrawableRes int rightTextIcon, boolean isRight) {
    this.rightTextIconPosition = isRight ? 0 : 1;
    setRightTextIcon(rightTextIcon);
  }

  public void setRightTextColor(@ColorInt int color) {
    inflateRightTextViewIfNeed();
    rightTextView.setTextColor(color);
  }

  public void setRightTextColor(ColorStateList colorStateList) {
    inflateRightTextViewIfNeed();
    rightTextView.setTextColor(colorStateList);
  }

  public void setRightTextStyle(@Nullable String font, int typeface) {
    inflateRightTextViewIfNeed();
    if (font != null) {
      rightTextView.setTypeface(Typeface.create(font, typeface));
    } else {
      rightTextView.setTypeface(null, typeface);
    }
  }

  public void setRightTextStyle(int typeface) {
    setRightTextStyle(null, typeface);
  }

  public void setRightIcon(@DrawableRes int drawableRes) {
    inflateRightIconViewIfNeed();
    rightIconView.setImageResource(drawableRes);
  }

  public void setRightIcon2(@DrawableRes int drawableRes) {
    inflateRightIcon2ViewIfNeed();
    rightIcon2View.setImageResource(drawableRes);
  }

  public void setRightIcon3(@DrawableRes int drawableRes) {
    inflateRightIcon3ViewIfNeed();
    rightIcon3View.setImageResource(drawableRes);
  }

  public void setRightIcon4(@DrawableRes int drawableRes) {
    inflateRightIcon4ViewIfNeed();
    rightIcon4View.setImageResource(drawableRes);
  }

  public void setRightIcon5(@DrawableRes int drawableRes) {
    inflateRightIcon5ViewIfNeed();
    rightIcon5View.setImageResource(drawableRes);
  }

  public void setRightTextSize(@Px int textSize) {
    inflateRightTextViewIfNeed();
    rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
  }

  public void setRightTextOnClickListener(View.OnClickListener listener) {
    inflateRightTextViewIfNeed();
    rightTextView.setOnClickListener(listener);
  }

  public void setRightIconOnClickListener(View.OnClickListener listener) {
    inflateRightIconViewIfNeed();
    rightIconView.setOnClickListener(listener);
  }

  public void setRightIcon2OnClickListener(View.OnClickListener listener) {
    inflateRightIcon2ViewIfNeed();
    rightIcon2View.setOnClickListener(listener);
  }

  public void setRightIcon3OnClickListener(View.OnClickListener listener) {
    inflateRightIcon3ViewIfNeed();
    rightIcon3View.setOnClickListener(listener);
  }

  public void setRightIcon4OnClickListener(View.OnClickListener listener) {
    inflateRightIcon4ViewIfNeed();
    rightIcon4View.setOnClickListener(listener);
  }

  public void setRightIcon5OnClickListener(View.OnClickListener listener) {
    inflateRightIcon5ViewIfNeed();
    rightIcon5View.setOnClickListener(listener);
  }

  /**
   * helper
   * */
  private <V extends View> V inflateIfNeed(V view, ViewStub viewStub) {
    if (view == null) {
      view = (V) viewStub.inflate();
    }

    return view;
  }

  private void inflateRightTextViewIfNeed() {
    rightTextView = inflateIfNeed(rightTextView, rightTextViewStub);
  }

  private void inflateRightIconViewIfNeed() {
    rightIconView = inflateIfNeed(rightIconView, rightIconViewStub);
  }

  private void inflateRightIcon2ViewIfNeed() {
    rightIcon2View = inflateIfNeed(rightIcon2View, rightIcon2ViewStub);
  }

  private void inflateRightIcon3ViewIfNeed() {
    rightIcon3View = inflateIfNeed(rightIcon3View, rightIcon3ViewStub);
  }

  private void inflateRightIcon4ViewIfNeed() {
    rightIcon4View = inflateIfNeed(rightIcon4View, rightIcon4ViewStub);
  }

  private void inflateRightIcon5ViewIfNeed() {
    rightIcon5View = inflateIfNeed(rightIcon5View, rightIcon5ViewStub);
  }

  private void inflateLeftTextViewIfNeed() {
    leftTextView = inflateIfNeed(leftTextView, leftTextViewStub);
  }

  private void inflateLeftIconViewIfNeed() {
    leftIconView = inflateIfNeed(leftIconView, leftIconViewStub);
  }

  private void inflateTitleViewIfNeed() {
    titleTextView = inflateIfNeed(titleTextView, titleTextViewStub);
  }

  private void inflateTitleIconViewIfNeed() {
    titleIconView = inflateIfNeed(titleIconView, titleIconViewStub);
  }

  /**
   * getters
   */
  public ImageView getLeftIconView() {
    return leftIconView;
  }

  public TextView getLeftTextView() {
    return leftTextView;
  }

  public TextView getTitleTextView() {
    return titleTextView;
  }

  public ImageView getTitleImageView() {
    return titleIconView;
  }

  public ImageView getRightIconView() {
    return rightIconView;
  }

  public ImageView getRightIcon2View() {
    return rightIcon2View;
  }

  public ImageView getRightIcon3View() {
    return rightIcon3View;
  }

  public ImageView getRightIcon4View() {
    return rightIcon4View;
  }

  public ImageView getRightIcon5View() {
    return rightIcon5View;
  }

  public TextView getRightTextView() {
    return rightTextView;
  }

  /**
   * Utils
   * */
  private static int sp2px(Context context, float sp) {
    float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
    return (int) (sp * fontScale + 0.5f);
  }

  private static int dp2px(Context context, int dp) {
    float density = context.getApplicationContext().getApplicationContext().getResources().getDisplayMetrics().density;
    return (int) (dp * density + 0.5f);
  }

  private static int getAttrResourceId(Context context, int name) {
    int[] textSizeAttr = new int[]{name};
    TypedArray a = context.obtainStyledAttributes(textSizeAttr);
    int value = a.getResourceId(0, 0);
    a.recycle();
    return value;
  }

  private static int getAttrDimensionPixelSize(Context context, int name) {
    int[] textSizeAttr = new int[]{name};
    TypedArray a = context.obtainStyledAttributes(textSizeAttr);
    int value = a.getDimensionPixelSize(0, 0);
    a.recycle();
    return value;
  }
}
