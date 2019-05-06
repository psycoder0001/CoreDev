package com.deepdroid.coredev.typo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.deepdroid.coredev.HelperForCommon;
import com.deepdroid.coredev.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TypoLayer extends RelativeLayout {
    private static final long SIZE_MULT = 5;
    private static final long FPS = 30; // 30 frames per sec.
    private static final long DRAW_INTERVAL = 1000 / FPS; // 33ms between drawings
    private static final long BUG_SPAWN_INTERVAL = 2000; // Spawn a bug each 1.5 sec
    private static final float SPEED_DEFAULT = (3f / FPS) * SIZE_MULT; // 3 px per sec.
    private static final float SPEED_INCREMENT = (0.025f / FPS) * SIZE_MULT; // 0.05f px per sec.
    private static final float DIFFICULTY_INCREMENT = 0.1f / FPS; // Increase difficulty by 0.1 per sec. ( Increase difficulty by one each 10 sec. )
    private static final float DIFFICULTY_MIN = 1;
    private static final float DEAD_BUG_DISAPPEAR_TIME = 2;

    private String[][] names = new String[][]{
            {"zax", "zek", "biz", "coz", "fez", "fiz", "jab", "jam", "jaw", "jib", "job", "jow", "jug", "pyx", "wiz", "zap", "zep", "zip", "haj", "jag", "jay", "jig", "jog", "joy", "jun"},
            {"moon", "four", "pole", "kill", "hope", "sole", "male", "juke", "mazy", "phiz", "qoph", "whiz", "zonk", "zouk", "zyme", "fuji", "futz", "fuze", "hazy", "jagg", "jake", "jaup", "java", "jerk", "jive", "joke", "jowl", "juba", "jube", "juco", "jupe", "koji", "puja", "putz", "quip", "zebu", "zeks", "zerk", "zinc", "bize", "boxy", "bozo", "czar", "dozy", "faze", "flux", "foxy", "friz", "hadj", "jabs", "jams", "jape", "jaws", "jeep", "jefe", "jehu", "jibe", "jibs", "jism", "jobs", "john", "jows", "juga", "jugs", "jury", "lazy", "maze", "meze", "mojo", "mozo", "pixy", "poxy", "prez", "quag", "quay", "quey", "spaz", "waxy"},
            {"sunny", "flare", "solar", "power", "trial", "heist", "grumpy", "mujik", "quack", "quick", "zappy", "zippy", "jumps", "kopje", "kudzu", "quaff", "quaky", "quiff", "zaxes", "zinky", "capiz", "enzym", "furzy"},
            {"patriot", "sleepy", "dreams", "nuclear", "energy", "faulty", "stungun", "muzzle", "puzzle", "terminator", "cumputer", "developer", "cupoftea", "machine", "intelligence", "bonner", "buzzer", "hurrah", "sneaky", "warrior", "klingon", "startrek", "vulcan", "federation", "starfleet", "creepy", "holly", "granade", "spirit", "sucker", "zergling", "protos", "colosus", "carrier", "zealot", "hydralisk", "ultralisk"}
    };

    private EditText editText;

    private Paint paintBugTorsoAlive;
    private Paint paintBugTorsoDead;
    private Paint paintBugLeg;
    private Paint paintBgBlack;
    private Paint paintTx;
    private Paint paintNameTx;
    private Paint paintLine;

    private int areaHeight;
    private int areaWidth;
    private int timeX;
    private int timeY;
    private int scoreX;
    private int scoreY;

    private Random random = new Random();
    private List<DamnBug> bugsIdle;
    private List<DamnBug> tempBugList;
    private List<DamnBug> bugsInAction;

    private boolean isFrameRunning = false;

    private int difficultyMax = names.length;
    private float currentBugSpeedPerFrame = SPEED_DEFAULT;
    private float currentSpawnTimeLeft = BUG_SPAWN_INTERVAL;
    private float currentDifficulty = 1;
    private float spawnLineY;
    private float deadLineY;
    private long currentScore;
    private long currentTime;
    private String currentInput;
    private Handler handler;

    public TypoLayer(Context context) {
        super(context);
        init(context, null);
    }

    public TypoLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TypoLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    private void init(Context context, AttributeSet attrs) {
        int colorDevGreen = getResources().getColor(R.color.development_green);

        setBackgroundColor(Color.BLACK);
        LayoutParams editTextParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextParams.addRule(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? RelativeLayout.ALIGN_PARENT_END : RelativeLayout.ALIGN_PARENT_RIGHT);
        editText = new EditText(context);
        editText.setBackgroundResource(R.drawable.dev_bg_button_black);
        editText.setLayoutParams(editTextParams);
        editText.setTextColor(colorDevGreen);
        editText.setPadding((int) editText.getTextSize(), 0, (int) editText.getTextSize(), 0);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentInput = s.toString();
            }
        });
        addView(editText);

        Button restartBtn = new Button(context);
        RelativeLayout.LayoutParams restartParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        restartParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        restartBtn.setLayoutParams(restartParams);
        restartBtn.setText("RESTART");
        restartBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        addView(restartBtn);

        areaWidth = HelperForCommon.getScreenWidth(context);

        paintBugTorsoAlive = new Paint();
        paintBugTorsoAlive.setColor(colorDevGreen);
        paintBugTorsoAlive.setAntiAlias(true);
        paintBugTorsoDead = new Paint();
        paintBugTorsoDead.setColor(getResources().getColor(R.color.development_gray));
        paintBugTorsoDead.setAntiAlias(true);
        paintBugLeg = new Paint();
        paintBugLeg.setColor(getResources().getColor(R.color.dev_clr_code_green_hint));
        paintBugLeg.setStyle(Paint.Style.STROKE);
        paintBugLeg.setStrokeWidth(2 * SIZE_MULT);
        paintBugLeg.setAntiAlias(true);

        paintBgBlack = new Paint();
        paintBgBlack.setColor(Color.BLACK);

        paintTx = new Paint();
        paintTx.setColor(colorDevGreen);
        paintTx.setTextSize(editText.getTextSize());
        paintTx.setAntiAlias(true);

        paintNameTx = new Paint(paintTx);
        paintNameTx.setTextAlign(Paint.Align.CENTER);

        paintLine = new Paint();
        paintLine.setColor(colorDevGreen);
        paintLine.setStrokeWidth(2 * SIZE_MULT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int textSize = (int) paintTx.getTextSize();
        areaWidth = r - l;
        areaHeight = b - t;
        spawnLineY = textSize * 1.5f;
        deadLineY = areaHeight * 0.5f;
        timeX = textSize / 2;
        timeY = (int) (spawnLineY + textSize);
        scoreX = areaWidth / 2;
        scoreY = (int) (spawnLineY + textSize);
    }

    public void start() {
        setVisibility(View.VISIBLE);
        bugsIdle = new ArrayList<>();
        tempBugList = new ArrayList<>();
        bugsInAction = new ArrayList<>();
        for (int x = 0; x < 50; x++) {
            bugsIdle.add(new DamnBug());
        }
        restart();
    }

    public void stop() {
//        setVisibility(GONE);
        isFrameRunning = false;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void restart() {
        isFrameRunning = true;
        currentSpawnTimeLeft = BUG_SPAWN_INTERVAL;
        currentBugSpeedPerFrame = SPEED_DEFAULT;
        currentDifficulty = DIFFICULTY_MIN;
        currentTime = 0;
        currentScore = 0;
        editText.setText("");
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
        handler = getHandler();
        frameRunnable.run();
    }

    private Runnable frameRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isFrameRunning) {
                return;
            }
            currentTime += DRAW_INTERVAL;
            currentBugSpeedPerFrame += SPEED_INCREMENT;
            currentDifficulty += DIFFICULTY_INCREMENT;
            if (currentDifficulty > difficultyMax) {
                currentDifficulty = difficultyMax;
            }
            tryBugSpawn();
            invalidate();
            getHandler().postDelayed(frameRunnable, DRAW_INTERVAL);
        }
    };
    // LIFE-CYCLE METHODS
    // =============================================================================================

    // =============================================================================================
    // COMMON METHODS
    private void tryBugSpawn() {
        currentSpawnTimeLeft -= DRAW_INTERVAL;
        if (currentSpawnTimeLeft <= 0) {
            if (bugsIdle.size() == 0) {
                return;
            }
            for (DamnBug bug : bugsIdle) {
                if (bug.isAvailableToSpawn()) {
                    bug.spawnBug(getDifficulty());
                    currentSpawnTimeLeft = BUG_SPAWN_INTERVAL;
                    bugsIdle.remove(bug);
                    bugsInAction.add(bug);
                    break;
                }
            }
        }
    }

    private int getDifficulty() {
        return random.nextInt((int) currentDifficulty);
    }

    private void onEndTypo() {
        stop();
    }
    // COMMON METHODS
    // =============================================================================================

    // =============================================================================================
    // DRAW METHODS
    @Override
    protected void onDraw(Canvas canvas) {
        if (bugsInAction != null && !bugsInAction.isEmpty()) {
            for (DamnBug bug : bugsInAction) {
                bug.calc(currentInput);
                bug.onDrawBug(canvas);
            }
            bugsInAction.removeAll(tempBugList);
            tempBugList.clear();
        }
        // Draw top spawn line.
        canvas.drawLine(0, spawnLineY, areaWidth, spawnLineY, paintLine);
        // Draw bottom dead line.
        canvas.drawLine(0, deadLineY, areaWidth, deadLineY, paintLine);
        // Draw time text.
        canvas.drawText(currentTime / 1000 + "", timeX, timeY, paintTx);
        // Draw score text.
        canvas.drawText(currentScore + "", scoreX, scoreY, paintTx);
        super.onDraw(canvas);
    }
    // DRAW METHODS
    // =============================================================================================

    // =============================================================================================
    // BUG METHODS
    private class DamnBug {
        private static final int BUG_STATE_WAITING_FOR_SPAWN = 0;
        private static final int BUG_STATE_ALIVE = 1;
        private static final int BUG_STATE_DEAD = 2;

        private float x = 0;
        private float y = 0;
        private String name;
        private int size;
        private int nameOffsetY;
        private DamnBugLeg[] legs;
        private float disappearTimeLeft = DEAD_BUG_DISAPPEAR_TIME;
        private int currentBugState = BUG_STATE_WAITING_FOR_SPAWN;

        private DamnBug() {

        }

        private void spawnBug(int sizeOfBug) {
            currentBugState = BUG_STATE_ALIVE;
            name = names[sizeOfBug][random.nextInt(names[sizeOfBug].length)];
            size = (int) (name.length() * SIZE_MULT);
            nameOffsetY = (int) (size * 1.5f);
            int spawnRangeX = areaWidth - size * 2;
            y = size;
            x = random.nextInt(spawnRangeX) + size;
            generateLegs();
        }

        private void generateLegs() {
            legs = new DamnBugLeg[6];
            legs[0] = new DamnBugLeg(size, 0, true);
            legs[1] = new DamnBugLeg(size, 1, true);
            legs[2] = new DamnBugLeg(size, 2, true);
            legs[3] = new DamnBugLeg(size, 0, false);
            legs[4] = new DamnBugLeg(size, 1, false);
            legs[5] = new DamnBugLeg(size, 2, false);
        }

        private void calc(String currentInput) {
            switch (currentBugState) {
                case BUG_STATE_ALIVE:
                    if (currentInput.toLowerCase().equals(name)) {
                        currentScore += currentInput.length();
                        editText.setText("");
                        name = "";
                        onBugDied();
                        return;
                    }
                    y += currentBugSpeedPerFrame;
                    if (y >= deadLineY) {
                        onBugDied();
                        onEndTypo();
                    }
                    break;
                case BUG_STATE_DEAD:
                    disappearTimeLeft -= DRAW_INTERVAL;
                    if (disappearTimeLeft <= 0) {
                        currentBugState = BUG_STATE_WAITING_FOR_SPAWN;
                        y = 0;
                        tempBugList.add(this);
                    }
                    break;
                case BUG_STATE_WAITING_FOR_SPAWN:
                    break;
            }
        }

        private void onDrawBug(Canvas canvas) {
            switch (currentBugState) {
                case BUG_STATE_ALIVE:
                    for (DamnBugLeg leg : legs) {
                        leg.noDrawLeg(canvas, x, y);
                    }
                    canvas.drawCircle(x, y, size, paintBugTorsoAlive);
                    canvas.drawText(name, x, y - nameOffsetY, paintNameTx);
                    break;
                case BUG_STATE_DEAD:
                    canvas.drawCircle(x, y, size, paintBugTorsoDead);
                    break;
            }
        }

        private void onBugDied() {
            disappearTimeLeft = DEAD_BUG_DISAPPEAR_TIME;
            currentBugState = BUG_STATE_DEAD;
        }

        private boolean isAvailableToSpawn() {
            return currentBugState == BUG_STATE_WAITING_FOR_SPAWN;
        }
    }
    // BUG METHODS
    // =============================================================================================

    // =============================================================================================
    // BUG LEG METHODS
    private class DamnBugLeg {
        private float offsetX;
        private float offsetY;

        private DamnBugLeg(int bugSize, int legIndex, boolean isLeft) {
            int legSize = 2 * bugSize;
            offsetX = isLeft ? -legSize : legSize;
            offsetY = -(bugSize / 2f) + (legIndex * (bugSize / 2f));
        }

        private void noDrawLeg(Canvas canvas, float bx, float by) {
            canvas.drawLine(bx, by, bx + offsetX, by + offsetY, paintBugLeg);
        }
    }
    // BUG LEG METHODS
    // =============================================================================================
}