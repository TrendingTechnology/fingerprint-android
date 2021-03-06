package com.fingerprintjs.android.fingerprint.signal_providers.device_state


import com.fingerprintjs.android.fingerprint.datasources.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.datasources.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.KeyGuardInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.SettingsDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class DeviceStateSignalProvider(
    settingsDataSource: SettingsDataSource,
    devicePersonalizationDataSource: DevicePersonalizationDataSource,
    keyGuardInfoProvider: KeyGuardInfoProvider,
    fingerprintSensorInfoProvider: FingerprintSensorInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalProvider<DeviceStateRawData>(version) {

    private val rawData =
        DeviceStateRawData(
            settingsDataSource.adbEnabled(),
            settingsDataSource.developmentSettingsEnabled(),
            settingsDataSource.httpProxy(),
            settingsDataSource.transitionAnimationScale(),
            settingsDataSource.windowAnimationScale(),
            settingsDataSource.dataRoamingEnabled(),
            settingsDataSource.accessibilityEnabled(),
            settingsDataSource.defaultInputMethod(),
            settingsDataSource.rttCallingMode(),
            settingsDataSource.touchExplorationEnabled(),
            settingsDataSource.alarmAlertPath(),
            settingsDataSource.dateFormat(),
            settingsDataSource.endButtonBehaviour(),
            settingsDataSource.fontScale(),
            settingsDataSource.screenOffTimeout(),
            settingsDataSource.textAutoReplaceEnable(),
            settingsDataSource.textAutoPunctuate(),
            settingsDataSource.time12Or24(),
            keyGuardInfoProvider.isPinSecurityEnabled(),
            fingerprintSensorInfoProvider.getStatus().stringDescription,
            devicePersonalizationDataSource.ringtoneSource(),
            devicePersonalizationDataSource.availableLocales().toList()
        )

    override fun fingerprint(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
    }

    override fun rawData() = rawData

    private fun v1(): String {
        val deviceStateSb = StringBuilder()
        deviceStateSb
            .append(rawData.adbEnabled)
            .append(rawData.developmentSettingsEnabled)
            .append(rawData.httpProxy)
            .append(rawData.transitionAnimationScale)
            .append(rawData.windowAnimationScale)

            .append(rawData.dataRoamingEnabled)
            .append(rawData.accessibilityEnabled)
            .append(rawData.defaultInputMethod)
            .append(rawData.rttCallingMode)
            .append(rawData.touchExplorationEnabled)

            .append(rawData.alarmAlertPath)
            .append(rawData.dateFormat)
            .append(rawData.endButtonBehaviour)
            .append(rawData.fontScale)
            .append(rawData.screenOffTimeout)
            .append(rawData.textAutoReplaceEnable)
            .append(rawData.textAutoPunctuate)
            .append(rawData.time12Or24)
            .append(rawData.isPinSecurityEnabled)
            .append(rawData.fingerprintSensorStatus)
            .append(rawData.ringtoneSource)

        rawData.availableLocales.forEach {
            deviceStateSb.append(it)
        }

        return hasher.hash(deviceStateSb.toString())
    }
}