package tj.behruz.savorcarTj.ui.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tj.behruz.savorcarTj.R
import tj.behruz.savorcarTj.databinding.PassengerBottomSheetBinding


class PassengerCountFragment(private val count:String,private val countHandler:(String)->Unit) : BottomSheetDialogFragment(){


    private lateinit var binding: PassengerBottomSheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dataBinding = DataBindingUtil.inflate<PassengerBottomSheetBinding>(inflater, R.layout.passenger_bottom_sheet, container, false)

        dataBinding.lifecycleOwner = this
        binding = dataBinding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setWhiteNavigationBar(dialog)
        }
        return dataBinding.root
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setWhiteNavigationBar(dialog: Dialog?) {
        val window: Window? = dialog?.window
        window?.let {
            val dimDrawable = GradientDrawable()
            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)
            val layers = listOf(dimDrawable, navigationBarDrawable).toTypedArray()
            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, getWindowHeight())
            window.setBackgroundDrawable(windowBackground)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passengerCount.text=count
        binding.minus.setOnClickListener {
            val count = binding.passengerCount.text.toString().toInt()
            if (count > 1) {
                binding.passengerCount.text = count.minus(1).toString()
            }
        }

        binding.plus.setOnClickListener {
            val count = binding.passengerCount.text.toString().toInt()
            if (count < 8) {
                binding.passengerCount.text = count.plus(1).toString()
            }
        }

        binding.floatingActionButton.setOnClickListener {
            countHandler.invoke(binding.passengerCount.text.toString())
            this.dismiss()
        }

        binding.closeIcon.setOnClickListener {
            this.dismiss()
        }

    }

    //    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    // override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
            // BottomSheetDialog(requireContext(), theme)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int { // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


}