package com.nets.applibs.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.nets.applibs.R
import java.util.*

class ActionSheetDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private var txt_title: TextView? = null
    private var txt_cancel: TextView? = null
    private var lLayout_content: LinearLayout? = null
    private var sLayout_content: ScrollView? = null
    private var showTitle = false
    private var sheetItemList: MutableList<SheetItem>? = null
    private val display: Display
    fun builder(): ActionSheetDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_sheet, null)
        view.minimumWidth = display.width
        sLayout_content = view.findViewById<View>(R.id.sLayout_content) as ScrollView
        lLayout_content = view.findViewById<View>(R.id.lLayout_content) as LinearLayout
        txt_title = view.findViewById<View>(R.id.txt_title) as TextView
        txt_cancel = view.findViewById<View>(R.id.txt_cancel) as TextView
        txt_cancel!!.setOnClickListener { dialog!!.dismiss() }
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog!!.setContentView(view)
        val dialogWindow = dialog!!.window
        dialogWindow!!.setGravity(Gravity.LEFT or Gravity.BOTTOM)
        val lp = dialogWindow.attributes
        lp.x = 0
        lp.y = 0
        dialogWindow.attributes = lp
        return this
    }

    fun setTitle(title: String?): ActionSheetDialog {
        showTitle = true
        txt_title!!.visibility = View.VISIBLE
        txt_title!!.text = title
        return this
    }

    fun setCancelable(cancel: Boolean): ActionSheetDialog {
        dialog!!.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): ActionSheetDialog {
        dialog!!.setCanceledOnTouchOutside(cancel)
        return this
    }

    /**
     *
     * @param strItem
     *
     * @param color
     *
     * @param listener
     * @return
     */
    fun addSheetItem(
        strItem: String?, color: SheetItemColor?,
        listener: OnSheetItemClickListener
    ): ActionSheetDialog {
        if (sheetItemList == null) {
            sheetItemList = ArrayList()
        }
        sheetItemList!!.add(SheetItem(strItem, color, listener))
        return this
    }

    private fun setSheetItems() {
        if (sheetItemList == null || sheetItemList!!.size <= 0) {
            return
        }
        val size = sheetItemList!!.size
        if (size >= 7) {
            val params = sLayout_content?.layoutParams as LinearLayout.LayoutParams
            params.height = display.height / 2
            sLayout_content!!.layoutParams = params
        }
        for (i in 1..size) {
            val sheetItem = sheetItemList!![i - 1]
            val strItem = sheetItem.name
            val color = sheetItem.color
            val textView = TextView(context)
            textView.text = strItem
            textView.textSize = 18f
            textView.gravity = Gravity.CENTER
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector)
                }
            } else {
                if (showTitle) {
                    if (i in 1 until size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector)
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector)
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector)
                    }
                }
            }
            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue.name))
            } else {
                textView.setTextColor(Color.parseColor(color.name))
            }
            val scale = context.resources.displayMetrics.density
            val height = (40 * scale + 0.5f).toInt()
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            textView.setOnClickListener {
                sheetItem.itemClickListener.onClick(i)
                dialog!!.dismiss()
            }
            lLayout_content!!.addView(textView)
        }
    }

    fun show() {
        setSheetItems()
        dialog!!.show()
    }

    interface OnSheetItemClickListener {
        fun onClick(which: Int)
    }

    inner class SheetItem(
        var name: String?, var color: SheetItemColor?,
        var itemClickListener: OnSheetItemClickListener
    )

    enum class SheetItemColor( name: String) {
        Blue("#4361A7"), Red("#FD4A2E"), BLACK("#000000");

    }

    init {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        display = windowManager.defaultDisplay
    }
}
/*
package com.nets.applibs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nets.applibs.R;

import java.util.ArrayList;
import java.util.List;


public class ActionSheetDialog {
	private Context context;
	private Dialog dialog;
	private TextView txt_title;
	private TextView txt_cancel;
	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private boolean showTitle = false;
	private List<SheetItem> sheetItemList;
	private Display display;

	public ActionSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ActionSheetDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_sheet, null);
		view.setMinimumWidth(display.getWidth());
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		lLayout_content = (LinearLayout) view
				.findViewById(R.id.lLayout_content);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}

	public ActionSheetDialog setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}

	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	/**
	 *
	 * @param strItem
	 *
	 * @param color
	 *
	 * @param listener
	 * @return
	 */
	public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color,
			OnSheetItemClickListener listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}


	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}

		int size = sheetItemList.size();

		if (size >= 7) {
			LayoutParams params = (LayoutParams) sLayout_content
					.getLayoutParams();
			params.height = display.getHeight() / 2;
			sLayout_content.setLayoutParams(params);
		}

		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;
			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(18);
			textView.setGravity(Gravity.CENTER);


			if (size == 1) {
				if (showTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}


			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue
						.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}


			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (40 * scale + 0.5f);
			textView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, height));


			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					dialog.dismiss();
				}
			});

			lLayout_content.addView(textView);
		}
	}


	public void show() {
		setSheetItems();
		dialog.show();
	}


	public interface OnSheetItemClickListener {
		void onClick(int which);
	}

	public class SheetItem {
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(String name, SheetItemColor color,
				OnSheetItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}

	public enum SheetItemColor {
		Blue("#4361A7"), Red("#FD4A2E"),BLACK("#000000");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}

 */