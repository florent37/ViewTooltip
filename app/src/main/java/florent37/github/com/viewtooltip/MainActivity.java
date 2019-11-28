package florent37.github.com.viewtooltip;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.florent37.viewtooltip.ViewTooltip;

public class MainActivity extends AppCompatActivity {

    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;

    @ColorInt
    public static final int GREEN = 0xFF5BBD76;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.editText);

        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final CheckBox customView = new CheckBox(MainActivity.this);
                //customView.setText("test");
                ViewTooltip
                        .on(editText)
                       // .customView(customView)
                        .position(ViewTooltip.Position.LEFT)
                        .arrowSourceMargin(0)
                        .arrowTargetMargin(0)
                        .text(getResources().getString(R.string.lorem))
                        .clickToHide(true)
                        .autoHide(false, 0)
                        .color(createPaint())
                        .animation(new ViewTooltip.FadeTooltipAnimation(500))
                        .onDisplay(new ViewTooltip.ListenerDisplay() {
                            @Override
                            public void onDisplay(View view) {
                                Log.d("ViewTooltip", "onDisplay");
                            }
                        })
                        .onHide(new ViewTooltip.ListenerHide() {
                            @Override
                            public void onHide(View view) {
                                Log.d("ViewTooltip", "onHide");
                            }
                        })
                        .show();
            }
        });

        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTooltip
                        .on(editText)
                        .autoHide(true, 1000)
                        .position(ViewTooltip.Position.RIGHT)
                        .text(getResources().getString(R.string.lorem))
                        .show();
            }
        });

        findViewById(R.id.top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTooltip
                        .on(editText)
                        .margin(50, 0, 50, 0)
                        .position(ViewTooltip.Position.TOP)
                        .text(getResources().getString(R.string.lorem))
                        .show();
            }
        });

        findViewById(R.id.bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewTooltip.TooltipView viewTooltip = ViewTooltip
                        .on(editText)
                        .color(Color.BLACK)
                        .distanceWithView(0)
                        .arrowHeight(0)
                        .arrowWidth(0)
                        .padding(20, 20, 20, 20)
                        .margin(50, 0, 50, 0)
                        .position(ViewTooltip.Position.BOTTOM)
                        .align(ViewTooltip.ALIGN.START)
                        .text(getResources().getString(R.string.lorem))
                        .show();

                //viewTooltip.close();
            }
        });

        findViewById(R.id.bottomRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTooltip
                        .on(v)
                        .color(Color.BLACK)
                        .position(ViewTooltip.Position.TOP)
                        .text("bottomRight bottomRight bottomRight")
                        .show();
            }
        });

        findViewById(R.id.bottomLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewTooltip
                        .on(v)
                        .color(Color.BLACK)
                        .position(ViewTooltip.Position.TOP)
                        .text("bottomLeft bottomLeft bottomLeft")
                        .show();
            }
        });
    }

    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0, 0, 0, 600, BLUE, GREEN, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }
}
