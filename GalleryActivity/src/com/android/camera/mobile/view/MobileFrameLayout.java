package com.android.camera.mobile.view;

import com.android.gallery3d.R;
import com.android.gallery3d.app.Log;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MobileFrameLayout extends LinearLayout {

	/**
	 * 是不是camera界面
	 */
	public static boolean isCamera = true;
	private View leftLayout;
	private View middleLayout;
	private View rightLayout;
	/** 屏幕的宽度 */
	private int screenWidth;
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	/** 左边布局的左边缘 */
	private int leftLeftEdge;
	/**
	 * 右边布局的右边缘
	 */
	private int rightRightEdge;

	/**
	 * 用于计算手指滑动的速度。
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * 手指按下的横坐标
	 */
	private float xDown;
	/**
	 * 手指移动时的横坐标
	 */
	private float xMove;
	/**
	 * 手指抬起时的横坐标
	 */
	private float xUp;

	/**
	 * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * leftLayout当前是显示还是隐藏。只有完全显示或隐藏leftLayout时才会更改此值，滑动过程中此值无效。
	 */
	// private boolean isLeftVisible;

	/**
	 * rightLayout当前是显示还是隐藏。只有完全显示或隐藏rightLayout时才会更改此值，滑动过程中此值无效。
	 */
	private boolean isRightVisible;

	/**
	 * leftLayout最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	 */
	private int leftRightEdge = 0;

	/**
	 * rightLayout最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	 */
	private int rightLeftEdge = 0;

	/**
	 * menu完全显示时，留给content的宽度值。
	 */
	private int menuPadding = 80;

	/** 左侧页面 */
	private static final int LEFT = 0;
	/** 中间页面 */
	private static final int MIDDLE = 1;
	/** 右边页面 */
	private static final int RIGHT = 2;
	/** 当前页面 */
	private int currentPage = MIDDLE;

	public MobileFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MobileFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MobileFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// super.onFinishInflate();

		WindowManager window = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = window.getDefaultDisplay().getWidth();

		leftLayout = this.findViewById(R.id.left_layout);
		middleLayout = this.findViewById(R.id.camera_root);
		rightLayout = this.findViewById(R.id.right_layout);
		
//		this.findViewById(R.id.myframe).setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
		
		leftLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		rightLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		

		middleLayout.getLayoutParams().width = screenWidth;
		leftParams = (LinearLayout.LayoutParams) leftLayout.getLayoutParams();
		rightParams = (LinearLayout.LayoutParams) rightLayout.getLayoutParams();

		leftParams.width = screenWidth;
		leftLeftEdge = -leftParams.width;
		leftParams.leftMargin = leftLeftEdge;

		rightParams.width = screenWidth;
		rightRightEdge = rightParams.width * 2;
		rightParams.rightMargin = rightRightEdge;

	}

	
	float downY = 0;
	float moveY = 0;
	
	float downX2 = 0;
	float moveX2 = 0;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			xDown = ev.getRawX();
			downY = ev.getRawY();
			if(ev.getPointerCount()>1){
				downX2 = ev.getX(1);
			}
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			
			moveY = ev.getRawY();
			xMove = ev.getRawX();
			
			if (ev.getPointerCount() > 1) {
				moveX2 = ev.getX(1);
			}
			
			
			if(currentPage == LEFT && Math.abs(xDown - xMove)<Math.abs(downY - moveY)){
				
				return super.onInterceptTouchEvent(ev);
			}
			
			if(ev.getPointerCount() >1 &&  xMove < xDown  && moveX2 < downX2){
				return true;
			}

			if (!isCamera || ev.getPointerCount() > 1) { // 如果当前不是拍照页面或者有多根手指在屏幕上，就允许滑动事件，否则禁止滑动
				return super.onInterceptTouchEvent(ev);
			}

			return true; // 此处return true，可以阻止事件向下传递
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
				try {
					downX2 = event.getX(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			

		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时，记录按下时的横坐标
			xDown = event.getRawX();
			if(event.getPointerCount()>1){
				downX2 = event.getX(1);
			}

			break;
		case MotionEvent.ACTION_MOVE:
			// 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整leftLayout的leftMargin值，从而显示和隐藏leftLayout
			xMove = event.getRawX();
			int disanceX = (int) (xMove - xDown);

			if (currentPage == RIGHT) {
				leftParams.leftMargin = leftLeftEdge * 2 + disanceX;
			} else if (currentPage == LEFT) {
				leftParams.leftMargin = disanceX;
			} else {
				leftParams.leftMargin = leftLeftEdge + disanceX;
			}

			if (leftParams.leftMargin < leftLeftEdge * 2) {
				leftParams.leftMargin = leftLeftEdge * 2;
			} else if (leftParams.leftMargin > leftRightEdge) {
				leftParams.leftMargin = leftRightEdge;
			}

			leftLayout.setLayoutParams(leftParams);
			break;
		case MotionEvent.ACTION_UP:
			// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
			xUp = event.getRawX();

			if (wantToShowLeft()) {
				if (shouldScrollToLeft()) {
					currentPage = LEFT;
					scrollToLeft();
				} else {
					currentPage = MIDDLE;
					scrollToMiddle(-30);
				}
			} else if (wantToShowRight()) {
				if (shouldScrollToRight()) {
					currentPage = RIGHT;
					scrollToRight();
				} else {
					currentPage = MIDDLE;
					scrollToMiddle(-30);
				}
			} else {
				if (currentPage == RIGHT) {
					if (xUp - xDown > 0) {
						currentPage = MIDDLE;
						scrollToMiddle(60);
					}
				} else {
					if (xUp - xDown < 0) {
						currentPage = MIDDLE;
						scrollToMiddle(-30);
					}
				}
			}

			// if (wantToShowMiddle()) {
			// if (shouldScrollToMiddle()) {
			// scrollToMiddle();
			// } else {
			// scrollToLeft();
			// }
			// }
			recycleVelocityTracker();
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
	 * 
	 * @return 当前手势想显示content返回true，否则返回false。
	 */
	private boolean wantToShowMiddle() {
		// return xUp - xDown < 0 && isLeftVisible;
		return xUp - xDown < 0;
	}

	/**
	 * 判断当前手势的意图是不是想显示content。如果手指移动的距离是负数，且当前menu是可见的，则认为当前手势是想要显示content。
	 * 
	 * @return 当前手势想显示content返回true，否则返回false。
	 */
	private boolean wantToShowRight() {
		return xDown - xUp > 0 && (currentPage == MIDDLE);
	}

	/**
	 * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是正数，且当前menu是不可见的，则认为当前手势是想要显示menu。
	 * 
	 * @return 当前手势想显示menu返回true，否则返回false。
	 */
	private boolean wantToShowLeft() {
		return xUp - xDown > 0 && (currentPage == MIDDLE);
	}

	/**
	 * 判断是否应该滚动将left展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
	 * 就认为应该滚动将left展示出来。
	 * 
	 * @return 如果应该滚动将menu展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToLeft() {
		return xUp - xDown > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 判断是否应该滚动将middle展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
	 * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将middle展示出来。
	 * 
	 * @return 如果应该滚动将content展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToMiddle() {
		return xDown - xUp + menuPadding > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 判断是否应该滚动将right展示出来。如果手指移动距离加上menuPadding大于屏幕的1/2，
	 * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将content展示出来。
	 * 
	 * @return 如果应该滚动将content展示出来返回true，否则返回false。
	 */
	private boolean shouldScrollToRight() {
		return xDown - xUp > screenWidth / 2
				|| getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 将屏幕滚动到left界面，滚动速度设定为30.
	 */
	private void scrollToLeft() {
		new ScrollTask().execute(30);
	}

	/**
	 * 将屏幕滚动到middle界面，滚动速度设定为-30.
	 */
	private void scrollToMiddle(int speed) {
		new ScrollTask().execute(speed);
	}

	/**
	 * 将屏幕滚动到right界面，滚动速度设定为-30.
	 */
	private void scrollToRight() {
		new ScrollTask().execute(-30);
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = leftParams.leftMargin;
			// 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (leftMargin > leftRightEdge) {
					leftMargin = leftRightEdge;
					break;
				}
				if (currentPage == MIDDLE) {
					if (leftMargin < leftLeftEdge) {
						leftMargin = leftLeftEdge;
						break;
					}
				} else if (currentPage == RIGHT) {
					if (leftMargin < leftLeftEdge * 2) {
						leftMargin = leftLeftEdge * 2;
						break;
					}
				}
				publishProgress(leftMargin);
				// 为了要有滚动效果产生，每次循环使线程睡眠10毫秒，这样肉眼才能够看到滚动动画。
				sleep(3);
			}

			// if (speed[0] > 0) {
			// isLeftVisible = true;
			// } else {
			// isLeftVisible = false;
			// }

			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {

			leftParams.leftMargin = leftMargin[0];
			leftLayout.setLayoutParams(leftParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			leftParams.leftMargin = leftMargin;
			leftLayout.setLayoutParams(leftParams);
		}
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 *            content界面的滑动事件
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 使当前线程睡眠指定的毫秒数。
	 * 
	 * @param millis
	 *            指定当前线程睡眠多久，以毫秒为单位
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
