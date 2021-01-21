package studio.kio.android.routeur.api.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import dalvik.system.DexFile
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.regex.Pattern

/**
 * created by KIO on 2021/1/20
 */
private const val EXTRACTED_NAME_EXT: String = ".classes"
private const val EXTRACTED_SUFFIX = ".zip"

private val SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes"

private const val PREFS_FILE = "multidex.version"
private const val KEY_DEX_NUMBER = "dex.number"

private const val VM_WITH_MULTIDEX_VERSION_MAJOR = 2
private const val VM_WITH_MULTIDEX_VERSION_MINOR = 1

private fun getMultiDexPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        PREFS_FILE,
        Context.MODE_PRIVATE or Context.MODE_MULTI_PROCESS
    )
}


/**
 * 通过指定包名，扫描包下面包含的所有的ClassName
 *
 * @param context     U know
 * @param packageName 包名
 * @return 所有class的集合
 */
@Throws(
    PackageManager.NameNotFoundException::class,
    IOException::class,
    InterruptedException::class
)
fun getFileNameByPackageName(context: Context, packageName: String): Set<String> {
    val classNames: MutableSet<String> = HashSet()
    val paths: List<String> = getSourcePaths(context)
    val parserCtl = CountDownLatch(paths.size)
    for (path in paths) {
        var dexFile: DexFile? = null
        try {
            dexFile = if (path.endsWith(EXTRACTED_SUFFIX)) {
                //NOT use new DexFile(path), because it will throw "permission error in /data/dalvik-cache"
                DexFile.loadDex(path, "$path.tmp", 0)
            } else {
                DexFile(path)
            }
            val dexEntries = dexFile?.entries()
            while (dexEntries?.hasMoreElements() == true) {
                val className = dexEntries.nextElement()
                if (className.startsWith(packageName)) {
                    classNames.add(className)
                }
            }
        } catch (ignore: Throwable) {
            Log.e("ARouter", "Scan map file in dex files made error.", ignore)
        } finally {
            try {
                dexFile?.close()
            } catch (ignore: Throwable) {
            }
            parserCtl.countDown()
        }
    }
    parserCtl.await()
    return classNames
}


/**
 * get all the dex path
 *
 * @param context the application context
 * @return all the dex path
 * @throws PackageManager.NameNotFoundException
 * @throws IOException
 */
@Throws(PackageManager.NameNotFoundException::class, IOException::class)
fun getSourcePaths(context: Context): List<String> {
    val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
    val sourceApk = File(applicationInfo.sourceDir)
    val sourcePaths: MutableList<String> = ArrayList()
    sourcePaths.add(applicationInfo.sourceDir) //add the default apk path

    //the prefix of extracted file, ie: test.classes
    val extractedFilePrefix = sourceApk.name + EXTRACTED_NAME_EXT

//        如果VM已经支持了MultiDex，就不要去Secondary Folder加载 Classesx.zip了，那里已经么有了
//        通过是否存在sp中的multidex.version是不准确的，因为从低版本升级上来的用户，是包含这个sp配置的
    if (!isVMMultiDexCapable()) {
        //the total dex numbers
        val totalDexNumber: Int = getMultiDexPreferences(context).getInt(KEY_DEX_NUMBER, 1)
        val dexDir = File(applicationInfo.dataDir, SECONDARY_FOLDER_NAME)
        for (secondaryNumber in 2..totalDexNumber) {
            //for each dex file, ie: test.classes2.zip, test.classes3.zip...
            val fileName =
                extractedFilePrefix + secondaryNumber + EXTRACTED_SUFFIX
            val extractedFile = File(dexDir, fileName)
            if (extractedFile.isFile) {
                sourcePaths.add(extractedFile.absolutePath)
                //we ignore the verify zip part
            } else {
                throw IOException("Missing extracted secondary dex file '" + extractedFile.path + "'")
            }
        }
    }
    return sourcePaths
}

/**
 * Identifies if the current VM has a native support for multiDex, meaning there is no need for
 * additional installation by this library.
 *
 * @return true if the VM handles multiDex
 */
private fun isVMMultiDexCapable(): Boolean {
    var isMultiDexCapable = false
    try {
        val versionString = System.getProperty("java.vm.version")
        if (versionString != null) {
            val matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString)
            if (matcher.matches()) {
                runCatching {
                    val major = matcher.group(1)?.toInt() ?: 0
                    val minor = matcher.group(2)?.toInt() ?: 0
                    isMultiDexCapable = major > VM_WITH_MULTIDEX_VERSION_MAJOR
                            || (major == VM_WITH_MULTIDEX_VERSION_MAJOR && minor >= VM_WITH_MULTIDEX_VERSION_MINOR)
                }
            }
        }
    } catch (ignore: Exception) {
    }

    return isMultiDexCapable
}