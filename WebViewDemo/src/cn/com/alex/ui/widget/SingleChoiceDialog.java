package cn.com.alex.ui.widget;

import cn.com.alex.R;
import cn.com.alex.utils.UIUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 单选按钮对话框，能判断其显隐状态
 * 
 * @author pengbosj
 * 
 */
@SuppressLint("NewApi")
public class SingleChoiceDialog extends AlertDialog implements
		android.view.View.OnClickListener, OnCheckedChangeListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

	/** 确定按钮 */
	public static final int BTN_OK = 0;
	/** 取消按钮 */
	public static final int BTN_CANCEL = 1;
	
	/** 大字体的id*/
	public static final int FONT_SIZE_BIG = R.id.topic_font_size_big;
	/** 中等字体的id*/
	public static final int FONT_SIZE_MIDDLE = R.id.topic_font_size_middle;
	/** 小字体的id*/
	public static final int FONT_SIZE_SMALL = R.id.topic_font_size_small;
	
	/** 用来记录对话框显隐状态 */
	private boolean mIsShowing = false;
	/** 确定和取消按钮 */
	private Button okBtn, cancelBtn;
	/** 单选按钮组合*/
	private RadioGroup mRadioButton;
	/** 按钮监听*/
	private OnDialogChangedListener mDialogChangedListener;
	
	private DisplayMetrics mDisplayMetrics;
	private int mScreenWidth;
	private int mScreenHeight;
	
	private Activity mContext;
	
	public SingleChoiceDialog(Activity context) {
		super(context);
		mContext = context;
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.topic_font_size_dialog, null);
		
		mScreenWidth = UIUtils.getScreenWidth(mContext.getWindowManager()); 
		mScreenHeight = UIUtils.getScreenHeight(mContext.getWindowManager()); 
		
		LayoutParams params = new LayoutParams(mScreenWidth*4/5, mScreenHeight/3);
		params.gravity = Gravity.CENTER;
		setView(view);
		
		mRadioButton = (RadioGroup) view.findViewById(R.id.topic_font_select);
		okBtn = (Button) view.findViewById(R.id.font_size_btn_ok);
		cancelBtn = (Button) view.findViewById(R.id.font_size_btn_cancel);
		mRadioButton.setOnCheckedChangeListener(this);
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}
	
	public void setOnDialogChangedListener(OnDialogChangedListener dialogChangedListener) {
		this.mDialogChangedListener = dialogChangedListener;
	}
	
	/**
	 * 设置被选中的RadioButton
	 * @param resId
	 */
	public void setChecked(int resId) {
		if(mRadioButton != null && mRadioButton.getChildCount() > 0) {
			mRadioButton.check(resId);
		}
	}
	
	/**
	 * 获取选中RadioButton的id
	 * @return id，返回-1表示RadioGroup中没有RadioButton，或者没有RadioButton被选中
	 */
	public int getChecked() {
		if(mRadioButton != null && mRadioButton.getChildCount() > 0) {
			return mRadioButton.getCheckedRadioButtonId();
		}
		return -1;
	}
	
	/**
	 * 对话框消失和显示
	 */
	public boolean isShowing() {
		return mIsShowing;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//点击确定按钮
		case R.id.font_size_btn_ok:
			mDialogChangedListener.onButtonClicked(BTN_OK);
			break;
		//点击取消按钮
		case R.id.font_size_btn_cancel:
			mDialogChangedListener.onButtonClicked(BTN_CANCEL);
			break;
		}
	}

	public interface OnDialogChangedListener {
		/**
		 * 当按钮被点击时触发
		 * @param which BTN_OK(0) ：表示点击了确定按钮
		 *   			BTN_CANCEL(1) : 表示点击了取消按钮
		 */
		public void onButtonClicked(int which);
		
		/**
		 * 当对话框中的单选按钮选项发生改变
		 * @param group RadioGroup
		 * @param checkedId 选中的RadioButton的id
		 */
		public void onCheckedChanged(RadioGroup group, int checkedId);
		
		public void onShow(DialogInterface dialog);
		
		public void onDismiss(DialogInterface dialog);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mDialogChangedListener.onCheckedChanged(group, checkedId);
	}

	@Override
	public void onShow(DialogInterface dialog) {
		mIsShowing = true;
		mDialogChangedListener.onShow(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		mIsShowing = false;
		mDialogChangedListener.onDismiss(dialog);
	} 
}
