package ak.android.movieshighlight.notification

import ak.android.movieshighlight.R
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class NotificationPreference : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_notification)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val notificationReceiver = NotificationReceiver()
        findPreference<SwitchPreferenceCompat>("daily")?.setOnPreferenceChangeListener { _, _ ->
            notificationReceiver.setDailyNotification(requireContext())
            true
        }
        findPreference<SwitchPreferenceCompat>("release")?.setOnPreferenceChangeListener { _, _ ->
            notificationReceiver.setDailyRelease(requireContext())
            true
        }
    }
}