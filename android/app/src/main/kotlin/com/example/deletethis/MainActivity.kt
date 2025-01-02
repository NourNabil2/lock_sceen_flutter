package com.example.deletethis

import android.app.KeyguardManager
import android.os.PowerManager;
import io.flutter.embedding.engine.FlutterEngine;

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.deletethis/lockscreen"

    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var componentName: ComponentName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Handle method calls from Flutter
        MethodChannel(flutterEngine!!.dartExecutor, CHANNEL).setMethodCallHandler { call, result ->

            if (call.method == "lockScreen") {
                lockScreen(result)
                result.success("Screen Locked")
            } else {
                Toast.makeText(this, "Lock Screen in errror else ", Toast.LENGTH_SHORT).show()
                result.notImplemented()
                result.error("ADMIN_NOT_ACTIVE", "Device Admin not active", null)

            }

        }

        devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        componentName = ComponentName(this, DeviceAdminReceiver::class.java)

        if (!devicePolicyManager.isAdminActive(componentName)) {
// If not an active admin, prompt the user to enable device admin
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable device admin to lock the screen")
            startActivityForResult(intent, 1)
        }

    }


    private fun lockScreen(result: MethodChannel.Result) {
        Toast.makeText(this, "I am here in lock scree", Toast.LENGTH_SHORT).show()
        if (devicePolicyManager.isAdminActive(componentName)) {
            Toast.makeText(this, "iiiiiiiiiiiiiiiiiiiii", Toast.LENGTH_SHORT).show()
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (keyguardManager.isKeyguardSecure) {
                Toast.makeText(this, "sssssssssssssssssssssssss", Toast.LENGTH_SHORT).show()
                devicePolicyManager.lockNow()
                Toast.makeText(this, "2122", Toast.LENGTH_SHORT).show()


            }


            result.success("Screen Locked")
        } else {
            Toast.makeText(this, "else else else else les", Toast.LENGTH_SHORT).show()
            result.error("ADMIN_NOT_ACTIVE", "Device Admin not active", null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Device Admin activated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Device Admin activation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}