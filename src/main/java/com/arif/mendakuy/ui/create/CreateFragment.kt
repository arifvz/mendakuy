package com.arif.mendakuy.ui.create

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arif.mendakuy.App
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Participant
import com.arif.mendakuy.data.model.Post
import com.arif.mendakuy.databinding.FragmentCreateBinding
import com.arif.mendakuy.ui.adapter.MountAdapter
import com.arif.mendakuy.ui.detailpost.DetailPostActivity
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    private lateinit var createViewModel: CreateViewModel
    private var _binding: FragmentCreateBinding? = null

    private val binding get() = _binding!!

    private val mountAdapter = MountAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewModel = ViewModelProvider(this).get(CreateViewModel::class.java)

        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dateSetListener1 =
            DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.dateStart.text = SimpleDateFormat("dd MM yyyy").format(date.time)
            }
        val dateSetListener2 =
            DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.dateFinish.text = SimpleDateFormat("dd MM yyyy").format(date.time)
            }

        val dialogdatepicker1 = DatePickerDialog(requireContext(), dateSetListener1, year, month, day)
        val dialogdatepicker2 = DatePickerDialog(requireContext(), dateSetListener2, year, month, day)

        binding.apply {
            dateStart.setOnClickListener{
                dialogdatepicker1.show()
            }

            dateFinish.setOnClickListener{
                dialogdatepicker2.show()
            }

            btnCreate.setOnClickListener {

                val strDate1 = binding.dateStart.text.toString()
                val date1: Date? = try {
                    SimpleDateFormat("dd MM yyyy").parse("$strDate1")
                } catch (e : Exception) {
                    null
                }

                val strDate2 = binding.dateFinish.text.toString()
                val date2: Date?= try{
                    SimpleDateFormat("dd MM yyyy").parse("$strDate2")
                } catch (e : Exception){
                    null
                }

                val participantUserName = arrayListOf<String>()
                App.getUserName()?.let { participantUserName.add(it)}

                val post = Post(
                    mount = mountAdapter.mountList,
                    postTitle = binding.inputTitle.text.toString(),
                    mountTrails = binding.inputMountVia.text.toString(),
                    meetPoint = binding.inputMeetPoint.text.toString(),
                    dateStart = date1,
                    dateFinish = date2,
                    user = App.getUser(),
                    participants = arrayListOf(
                        Participant(
                            user = App.getUser(),
                            status = K.STATUS_HOST,
                            message = "  ",
                            requestDate = date1
                        )),
                    participantsUsername  = participantUserName,
                )
                createViewModel.onCreate(post)

                inputTitle.text.clear()
                inputMountVia.text.clear()
                inputMeetPoint.text.clear()
                dateStart.setText(" ")
                dateFinish.setText(" ")
            }
            mountList.adapter = mountAdapter
            mountList.layoutManager = GridLayoutManager(context, 4)
        }

        createViewModel.getMount()
        createViewModel.mountList.observe(viewLifecycleOwner) {
            mountAdapter.setData(it)
        }

        createViewModel.detailPost.observe(viewLifecycleOwner) {
            if (it != null){
                val intent = Intent(context, DetailPostActivity::class.java)
                intent.putExtra("id", it)
                activity?.startActivity(intent)
            }
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result : ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data
        }

    }



}