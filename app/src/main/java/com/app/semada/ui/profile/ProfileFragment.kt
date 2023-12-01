package com.app.semada.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.semada.DataStore
import com.app.semada.databinding.FragmentProfileBinding
import com.app.semada.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val preferenceDataStore = DataStore(requireContext())
        val data = preferenceDataStore.getLoggedData()
        val nama = data.first
        val kelas = data.second
        binding.profileNama.text = nama
        binding.profileKelas.text = kelas

        binding.buttonUbahpass.setOnClickListener {
            startActivity(Intent(requireContext(), UbahPassActivity::class.java))
            requireActivity().finish()
        }
        binding.buttonTentangapp.setOnClickListener {
            startActivity(Intent(requireContext(), TentangActivity::class.java))
            requireActivity().finish()
        }
        binding.buttonLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return root

    }

    private fun showLogoutConfirmationDialog() {
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Keluar Akun")
            builder.setMessage("Apakah kamu yakin ingin keluar?")

            builder.setPositiveButton("Ya") { dialog, which ->
                context?.getSharedPreferences("UserData", 0)?.edit()?.clear()?.commit();
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }

            builder.setNegativeButton("Tidak") { dialog, which ->
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}