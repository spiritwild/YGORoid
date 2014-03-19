package org.msk86.ygoroid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.msk86.ygoroid.R;
import org.msk86.ygoroid.newcore.Item;
import org.msk86.ygoroid.newutils.BmpReader;
import org.msk86.ygoroid.newutils.Configuration;
import org.msk86.ygoroid.newutils.FPSMaker;
import org.msk86.ygoroid.newutils.Utils;
import org.msk86.ygoroid.size.OtherSize;

public abstract class YGOView extends SurfaceView implements Runnable {
    private static Bitmap BACKGROUND_BMP;

    static {
        BACKGROUND_BMP = BmpReader.readBitmap(Configuration.texturePath() + "bg" + Configuration.cardImageSuffix(), R.raw.bg, OtherSize.SCREEN);
    }

    private static final int ACTIVE_DRAW_DURATION = 1500;
    private Thread renderThread;
    private SurfaceHolder holder;
    private boolean running = false;
    private FPSMaker fpsMaker;
    private long actionTime;

    public YGOView(Context context, AttributeSet attrs) {
        super(context, attrs);
        renderThread = new Thread(this);
        holder = getHolder();
        this.setLongClickable(true);
        fpsMaker = new FPSMaker();
    }


    protected void drawVersion(Canvas canvas) {
//        TextPaint paint = new TextPaint();
//        paint.setColor(Configuration.fontColor());
//        int fontSize = Utils.unitLength() / 7;
//        paint.setTextSize(fontSize);
//        paint.setAntiAlias(true);
//        StaticLayout layout = new StaticLayout("V" + Utils.getVersion(), paint, Utils.unitLength() / 2, Layout.Alignment.ALIGN_OPPOSITE, 1, 0, false);
//        Utils.DrawHelper helper = new Utils.DrawHelper(0, 0);
//        helper.drawLayout(canvas, layout, Utils.unitLength() * 11 / 2, Utils.screenHeight() - fontSize - 2);
    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        Utils.DrawHelper helper = new Utils.DrawHelper(0, 0);
        if (BACKGROUND_BMP != null) {
            canvas.drawBitmap(BACKGROUND_BMP, helper.center(Utils.screenWidth(), BACKGROUND_BMP.getWidth()), 0, new Paint());
        }
    }

    abstract protected void doDraw(Canvas canvas);

    @Override
    public void run() {
        fpsMaker.setTime(System.nanoTime());
        while (running) {
            fpsMaker.setLimitTime(System.currentTimeMillis());
            if (actionTime + ACTIVE_DRAW_DURATION >= System.currentTimeMillis()) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        doDraw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

            }

            fpsMaker.limitFPS();
            fpsMaker.makeFPS();
        }
    }

    public void resume() {
        running = true;
        updateActionTime();
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void pause() {
        running = false;
        deallocateMemory();
    }

    public boolean isRunning() {
        return running;
    }

    public void updateActionTime() {
        actionTime = System.currentTimeMillis();
    }

    public abstract OnKeyProcessor getOnKeyProcessor();
    public abstract OnMenuProcessor getOnMenuProcessor();

    public static final String DUEL_STATE_LOGO = "DUEL_STATE_LOGO";
    public static final String DUEL_STATE_DUEL = "DUEL_STATE_DUEL";
    public static final String DUEL_STATE_SIDE = "DUEL_STATE_SIDE";
    public static final String DUEL_STATE_DECK = "DUEL_STATE_DECK";
    public abstract String getDuelState();

    public abstract Item exportData();
    public abstract void importData(Item item);

    public abstract void deallocateMemory();
}
