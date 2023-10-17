package com.example.android.boredombuddy.favourites

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.boredombuddy.BuildConfig
import com.example.android.boredombuddy.R
import com.example.android.boredombuddy.databinding.FragmentFavouritesBinding
import com.example.android.boredombuddy.utils.getAlarmManager
import com.example.android.boredombuddy.utils.makePendingIntent
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject


class FavouritesFragment : Fragment() {

    private lateinit var favouritesBinding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by inject()

    private val notificationPermissionRequestLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.postToast(getString(R.string.permission_granted))
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.permission_dialog_title))
                        .setMessage(getString(R.string.permission_dialog_message))
                        .setPositiveButton(
                            getString(R.string.accept)
                        )
                        { dialog, _ ->
                            viewModel.postToast(getString(R.string.permission_dialog_success_toast))
                            dialog.dismiss()
                        }
                        .setNegativeButton(
                            getString(R.string.decline)
                        ) { dialog, _ ->
                            raisePermissionDeniedSnackBar()
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    raisePermissionDeniedSnackBar()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        favouritesBinding.lifecycleOwner = this
        favouritesBinding.viewModel = viewModel
        val adapter = FavouritesListAdapter(
            {suggestion ->
                val pendingIntent = makePendingIntent(requireContext().applicationContext, suggestion)
                requireContext().getAlarmManager().cancel(pendingIntent)
                viewModel.deleteSuggestion(suggestion.id)
            },
            {suggestion ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                ) {
                    notificationPermissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    findNavController().navigate(R.id.action_viewPagerFragment_to_setNotification, bundleOf("suggestion" to suggestion))
                }
            },
            {suggestion ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_suggestion, suggestion.activity))
                }
                if(intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(intent)
                }
            }

        )

        favouritesBinding.favouritesRecyclerView.adapter = adapter

        return favouritesBinding.root
    }

    private fun raisePermissionDeniedSnackBar() {
        Snackbar.make(
            requireView(),
            getString(R.string.permission_snackbar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.settings)) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data =
                        Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }

}