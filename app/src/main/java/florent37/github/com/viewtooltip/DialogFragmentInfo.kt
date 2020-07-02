package florent37.github.com.viewtooltip

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.github.florent37.viewtooltip.ViewTooltip

class DialogFragmentInfo : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_fragment, container, false).also {


            it.findViewById<Button>(R.id.top).setOnClickListener(View.OnClickListener {
                ViewTooltip
                        .on(this,  it.findViewById<Button>(R.id.top))
                        // .customView(customView)
                        .position(ViewTooltip.Position.TOP)
                        .arrowSourceMargin(0)
                        .arrowTargetMargin(0)
                        .text(resources.getString(R.string.lorem))
                        .clickToHide(true)
                        .autoHide(false, 0)
                        .animation(ViewTooltip.FadeTooltipAnimation(500))
                        .onDisplay(ViewTooltip.ListenerDisplay { Log.d("ViewTooltip", "onDisplay") })
                        .onHide({ Log.d("ViewTooltip", "onHide") })
                        .show()
            })
        }
        return v
    }

    override fun getDialog(): Dialog {
        return super.getDialog()
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}