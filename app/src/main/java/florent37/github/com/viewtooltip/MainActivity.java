package florent37.github.com.viewtooltip;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.florent37.viewtooltip.ViewTooltip;

public class MainActivity extends AppCompatActivity {

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
                        .text(getResources().getString(R.string.lorem))
                        .clickToHide(true)
                        .autoHide(false, 0)
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
                        .padding(20, 20, 20, 20)
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
}
