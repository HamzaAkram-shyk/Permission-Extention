package com.base.utill

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified R : ActivityResultLauncher<Array<String>>> Fragment.requestPermission(
    permission: List<String>,
    noinline grantedAll: (permission: List<String>) -> Unit = {},
    noinline denied: (permission: String) -> Unit = {},
): ReadOnlyProperty<Fragment, R> =
    PermissionResultDelegate(this, permission.toTypedArray(), grantedAll, denied)


class PermissionResultDelegate<R : ActivityResultLauncher<Array<String>>>(
    private val fragment: Fragment,
    private val permission: Array<String>,
    private val grantedAll: (permission: List<String>) -> Unit,
    private val denied: (permission: String) -> Unit,
) : ReadOnlyProperty<Fragment, R> {

    private var permissionResult: ActivityResultLauncher<Array<String>>? = null


    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.apply {
                    permissionResult = registerForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        if (permissions.all { it.value }) {
                            grantedAll(permission.toList())
                            return@registerForActivityResult
                        } else {
                            val permission = permissions.entries.find { !it.value }
                            if (permission != null) {
                                denied(permission.key)
                                return@registerForActivityResult
                            }
                        }
                    }
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                permissionResult = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): R {

        permissionResult?.let { return (it as R) }

        error("Failed to Initialize Permission")
    }
}