package com.monthlywriting.android.beta.ui.goal.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.databinding.FragmentPhotoDetailBinding
import com.monthlywriting.android.beta.util.getBitmapFromPath
import java.util.ArrayList

class PhotoDetailFragment : Fragment(), MomentzCallback {

    private var _binding: FragmentPhotoDetailBinding? = null
    private val binding get() = _binding!!

    private var list: List<String> = listOf()
    private var startingIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        initializeData()
        startMomentz()
    }

    private fun initializeData(){
        arguments?.let {
            list = it.getStringArrayList(FILEPATH_LIST) as List<String>
            startingIndex = it.getInt(CLICKED_POSITION)

            binding.tvGoal.text = it.getString(MOMENTZ_TITLE)!!
        }
    }

    private fun startMomentz() {
        val viewList = mutableListOf<MomentzView>()

        for (filePath in list) {
            val imageView = ImageView(requireContext())
            imageView.setImageBitmap(getBitmapFromPath(filePath))
            viewList.add(MomentzView(imageView, 5))
        }

        val momentz = Momentz(requireContext(),
            viewList,
            binding.container,
            this,
            R.drawable.momentz_bar_color)

        momentz.start()

        if (startingIndex != 0) {
            for (i in 0 until startingIndex) {
                momentz.next()
            }
        }
    }

    override fun done() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {}

    companion object {
        @JvmStatic
        fun newInstance(index: Int, list: List<String>, title: String) =
            PhotoDetailFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(FILEPATH_LIST, list as ArrayList<String>)
                    putInt(CLICKED_POSITION, index)
                    putString(MOMENTZ_TITLE, title)
                }
            }

        private const val FILEPATH_LIST = "filepath list"
        private const val CLICKED_POSITION = "clicked position"
        private const val MOMENTZ_TITLE = "momentz title"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}