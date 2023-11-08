package com.arif.mendakuy.ui.detailpost

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.App
import com.arif.mendakuy.data.K
import com.arif.mendakuy.data.model.Participant
import com.arif.mendakuy.databinding.ActivityDetailPostBinding
import com.arif.mendakuy.ui.adapter.ParticipantAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat


class DetailPostActivity : AppCompatActivity() {
    private lateinit var detailPostViewModel: DetailPostViewModel
    private var _binding: ActivityDetailPostBinding? = null

    private val binding get() = _binding!!

    private val participantAdapter = ParticipantAdapter(arrayListOf())
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailPostViewModel =
            ViewModelProvider(this).get(DetailPostViewModel::class.java)

        _binding = ActivityDetailPostBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        onViewCreated()
    }

    fun onViewCreated() {
        binding.apply {
            playerView.adapter = participantAdapter
            playerView.layoutManager = LinearLayoutManager(this@DetailPostActivity)

            progressBar = ProgressBar(this@DetailPostActivity)

            detailPostViewModel.getDetailPost(
                intent.getStringExtra("id")
            )
            participantAdapter.callback = object : ParticipantAdapter.Callback {
                override fun onClickAccept(participant: Participant) {
                    detailPostViewModel.onUpdateStatus(participant, status = K.STATUS_ACCEPT)
                }
                override fun onClickDecline(participant: Participant) {
                    detailPostViewModel.onUpdateStatus(participant, status = K.STATUS_DECLINE)
                }
            }

            detailPostViewModel.state.observe(this@DetailPostActivity) { state ->
                when (state) {
                    is DetailPostViewState.Progress -> onProgress(state.isLoading)
                    is DetailPostViewState.DeleteResult -> onDeleteResult(state.message)
                }
            }

            detailPostViewModel.detailPost.observe(this@DetailPostActivity) { post ->
                binding.apply {

                    val dateS: String? = try {
                        post.dateStart?.let {
                            SimpleDateFormat("dd MMM yyyy").format(it)
                        }
                    } catch (e: Exception) {
                        "-"
                    }
                    val dateF: String? = try {
                        post.dateFinish?.let {
                            SimpleDateFormat("dd MMM yyyy").format(it)
                        }
                    } catch (e: Exception) {
                        "-"
                    }

                    mountName.text = post.mount?.mountName
                    mountTrails.text = post.mountTrails
                    meetPoint.text = post.meetPoint
                    postTitle.text = post.postTitle
                    dateStart.text = dateS
                    dateFinish.text = dateF
                    location.text = post.mount?.mountLoc

                    Glide.with(ivMount).load(post.mount?.mountImg).transform(RoundedCorners(20)).into(ivMount)

                    participantAdapter.isShowButton = post.user?.username == App.getUserName()
                    participantAdapter.setData(post.participants)

                    btnDelete.setOnClickListener {
                        val alertDialog = AlertDialog.Builder(this@DetailPostActivity)
                        alertDialog.setTitle("DELETE")
                        alertDialog.setMessage("Are you sure you want to Delete this Post?")
                        alertDialog.setPositiveButton(
                            "YES",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    detailPostViewModel.deletePost(post)
                                }
                            })
                        alertDialog.setNegativeButton(
                            "NO",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog?.dismiss()
                                }
                            })
                        alertDialog.show()
                    }
                }
            }

            binding.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun onDeleteResult(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun onProgress(loading: Boolean) {
        if (loading) {
            progressBar.isVisible
        } else {
            progressBar.isInvisible
        }
    }
}
