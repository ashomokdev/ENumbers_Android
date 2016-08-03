package com.ashomok.eNumbers;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.test.ActivityInstrumentationTestCase2;
import android.test.IsolatedContext;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.activities.TaskDelegate;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskRESTClient;
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTaskStandalone;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;
import com.ashomok.eNumbers.data_load.ENumbersSQLiteAssetHelper;

import junit.framework.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/ENumbers/";
    public static final String TEST_IMGS = "test_imgs";

    public static final String TAG = "MainActivityTest";

    public MainActivityTest() {

        super(MainActivity.class);
    }

    public void testSQLiteAssetHelper() {

        IsolatedContext _openHelperContext = new IsolatedContext(new MockContentResolver(), getActivity());

        ENumbersSQLiteAssetHelper helper = new ENumbersSQLiteAssetHelper(_openHelperContext);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100"}).getCount() == 1);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100", "E123"}).getCount() == 2);

        Assert.assertTrue(helper.selectRowsByCodes(new String[]{"E100", "E123", "E101a"}).getCount() == 3);
    }

    public void testOCREngineRecognize() throws IOException {
        ArrayList<String> files = getTestImages();

        OCREngineImpl ocrEngine = new OCREngineImpl();

        for (String file : files) {
            String result = ocrEngine.RetrieveText(getActivity().getApplicationContext().getAssets(),
                    file);

            appendLog("Result from " + file + result);
        }
    }

    public void testOCREngineParse() {
        String result1 = "Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0079.JPG' \"1 _' ' .u1 '1' \\ 1w 11 1\n" +
                "\n" +
                "1v. . - 1 V. '~17\n" +
                "1,1\" . = QQLCA/sg ). Iogere' ko\"~'=~ 1, '\n" +
                ".4. Imarynowane,( zaWIera|qI gg'g), ocet NZ\" $1441 V\n" +
                "BMUjace: E 202, E 211. regu atorkwasowoeci: 2 u\n" +
                "mCJe dezqce, E 951, E 950. E 954 E 955, balwniki: i\n" +
                "1 _Wzmacniacz smaku: E 621.\n" +
                "olowyrn( 10 g). Sk1adn|k|: wodasoja, pszenmmzawiera\n" +
                "rae. mieczaki selg'\n" +
                "erze ochronnej. Produk1 do U82PL')51?:L]1|1??QO SpOZ/Cld";

        String result2 = "E200 ftsjs, sssks";

        String result3 = "Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0078.JPGZestaw Sushi _ ' \" '\n" +
                "o 1WKPWV\n" +
                "m fawvcra cukxer x substanqe squce Zavuera Zrcdio fenybamnmy\n" +
                "Wadmkl W1 gowwany lpi??weclzony na znnnov rzodkmw marynowana\n" +
                "Immr marynowany sanik Slmmlankoqu zawuara mlgkiygmlgklr?),\n" +
                "@19ng mgrizony m mnno \"(1mm sunml [Z?'w/le?ajd Mimi 9mm\n" +
                "ekqwm> wagatu l 'J'VHnimeld gggeyig. , ogerek konsengwy nun\n" +
                "1mg \"101:1ka paw dym mmynw/ane> ZanGleil'd 5919:) ocehyzow (:ukxer\n" +
                "ahl uumhuw:knnm-rwuyacw F 402 F \"l1 ugu?aim kwasowowzl ENG\n" +
                "17.110 F \\ 70 auhsmnqe Mod/m e [1AM [1150 F 'ML E {J55 mwmk'\n" +
                "'1 __ L: IUI I' 150 'r, MR \\Mmacmauz smdku F 62'\n" +
                "baa/elm z swsem 5,010':wa '\\O g' EVA/\"hum wok: sold pvmm \\x'wmem\n" +
                "qluien/ am\n" +
                "Pmduki VHOZt zavnem? ruggwggki\n" +
                "(\"kw/an,' w atmosferzv onln w m1 I'rmukrlrwu MAM w MM,\" gpuzwhl\n" +
                "'sax Hello /, PL ,\\>\\ '\n" +
                "M1.\" \".3112 g K\"\".\"L\"°' ) H\\HliHIIIIHHIHIHIHH\"\n";

        String result4 = "Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0088.JPG, , . .\n" +
                "Skhdnlkl: mqka PSZENNA, mm kokoluwy mas) mam\n" +
                "knkulnwe (0,51%), cukler, puneryxnwalle JAJA. won. mm\n" +
                "[:ukler, ikrubla mady?knwana (E1414), gluknzl (kukury?l),\n" +
                "MLEKO w pmlzku oduuilczone, mam MLEKA, Iuhmncll\n" +
                "laggszczalace [5401, 545ml), 5327, 5833\") EA\", E3330\"),\n" +
                "56L substancje konterwujqce (Ezun, 5202), nzrwmuecmm\n" +
                "arumat wanlliuwy)' ale] sinnecznlknwy, suhmncjl\n" +
                "kunserwujqcu [2202, Ezuu), arumal unknown, wads, Nunez\n" +
                "(oelmny [rc5lmny ole| shonecxmkawy, milmny ?uszcz\n" +
                "palmbwy' Wadi' 561' regulator kwuuwcsm (Esau), sunsunq:\n" +
                "konserwumca (52011)' mulgaxory (2474, llcylyny z sol),\n" +
                "aromat ma§\\any, prieciwu?amacze (new; Ems], bllwmk\n" +
                "\n" +
                "'(E1SCa(i]]' cukler, waiter/lawn\": JAM mu, lub?incla\n" +
                "kcnserwujqca {mm rlgmnor szsnwo§c\\ [sum]. drums.\n" +
                "syrup glukexnwo-fruktqmwy, mun\" plekarnlcza\n" +
                "[emulgawry (E471, E461),'g\\u\\en PSZENNY, \"\\ku PSZENNA,\n" +
                "substancja lagQSXCZ??C' [EASE], aroma! wlnlllbwy. eradak dB\n" +
                "prlelwariama mqkllEQFD], birw\"lk(E1U\\]],QNKOXG,FHJK1I11I'\n" +
                "sel' substancja kmlserwumca 152m cukIer pun\" mm\n" +
                "Zawmrae 5an\n" +
                "wmase uni-1w\"a w mag wmosi: energetyczna' uzsu /\n" +
                "\"um; TlusL, 13.4w\" Iym kwasy (iuslrzuwe nasycone'\n" +
                "IW. WWMM Mm MN .m mm";

        String result5 =
                "Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0080.JPG1 \"II 1\n" +
                        "Carefuily pull outthe\n" +
                        "right SIM card (mended,\n" +
                        "microS'iF/W 0!' \"~31\n";

        String result6 = "\"Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0081.JPGJ' \\\" I' : PLAY .< 3;!' 'p' ..~ ' Q I, ':72 V\\n\" +\n" +
                "                \"' alumni PLNS \\\\ n =1 i\\\" w. Es PLNOZ'; mung Puma pm'\\n\" +\n" +
                "                \"gPLAY iiiiiiiiii I'\\n\" +\n" +
                "                \"7:1\\\"; 37;;ng PLNS PLNnZQ PLNCU'! PLNUZQ PLNDOQ uNuWEn szm M\\n\" +\n" +
                "                \"Activate unlimited calls and texts \\\"1+;\\n\" +\n" +
                "                \"t0 Play users by dialling *188#<. STAN I\\n\" +\n" +
                "                \"on your phone \\\"ARMOWY '\\n\" +\n" +
                "                \"_\\n\" +";

        String result7 = " \"Result from /storage/emulated/0/ENumbers/test_imgs/DSC_0087.JPG_,,!,v_,_,-=,~=1 = =1\\n\" +\n" +
                "                \"'\\\"-ii. ~r=§sExiegegi?gnig?eag- 'R\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"'e?i? §§§F$~45§§§ §§;§§'2\\\"E52i%i 5' \\\\5\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \":92; SggjgmigaQEi'?ii?iiEE-ver % '5\\\"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"zen.> {1: B=§=~§sea?s?gmag? ° =\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \".33> n.Eav§\\\"%a????§i_P§a§;-:i§5 N\\\";\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \".,,-z OH>: \\\"mum'mngoqqva >3\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"=52. i;-<_~r {53' SS Hg?'>§%2'§2w a r\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"2:5 rs?' H?g?ii?lm'i'ge?? \\\\\\\" M 'T'\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"g z $26 ;_Ni_,_n > a ,9\\n\" +\n" +
                "                \"F .3 555' ?fgggiuiiggg $525: g '3\\n\" +\n" +
                "                \"E _5 g - \\\"355';$§K§Ragg???iig§ -\\\"' m\\\"\\n\" +\n" +
                "                \"' g \\\"ii E?!\\\" :H?signgng \\\\ ' 3°\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"w g 5:: :?e- - 5*\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"2 ~< -== '- gg,gfg_.;ygggku j 4 a\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"m-a a '=2 0:11.\\\" _ - a\\\": = 5 B ' ru-\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"n_ l.- 95.: 5- =-<=*T;-m 25w\\\": ~ _ o n.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \">-__?,_-,a \\\"a; '2:55,?! \\\"as? gm;~_>~ /' >\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"$:-2=5 \\\"5-; \\\"55'; $r§3§§§ zieiI-F' v\\\\ W 3'6'\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \";-gg§ §-= §=§;E;.;:gq:,-n;n:;s- 55\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"F _=,- _-,..=~.,-,_ n.-\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\\"=32 2:: wig??gi aEEEsEHKZ \\\"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"wa\\\"\\\" FE\\\" g§~zig=§hsnae?un N\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"u--_--'i P92 °~Fzg>g_g???z;?_=w=-ar\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"u-Q\\\" u:l- ~.,g_>...--rr'~\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"~53 9.!_ ~-\\n\" +";


        String result8 = "E200";

        String result9 = "E101a, E200, E1414";

//        String result10 = "E401, E4506"; //fails

        OCREngine ocrEngine = new OCREngineImpl();

        Assert.assertTrue(
                new HashSet<String>(Arrays.asList("E202", "E211", "E950", "E951", "E954", "E955", "E621")).equals(ocrEngine.parseResult(result1)));

        Assert.assertTrue(
                new HashSet<String>(Arrays.asList("E200")).equals(ocrEngine.parseResult(result2)));

        Assert.assertTrue(
                new HashSet<String>().equals(ocrEngine.parseResult(result3)));

        Assert.assertTrue(
                new HashSet<String>(Arrays.asList("E1414", "E3330", "E471", "E461")).equals(ocrEngine.parseResult(result4)));

        Assert.assertTrue(
                new HashSet<String>().equals(ocrEngine.parseResult(result5)));

        Assert.assertTrue(
                new HashSet<String>().equals(ocrEngine.parseResult(result6)));

        Assert.assertTrue(
                new HashSet<String>().equals(ocrEngine.parseResult(result7)));

        Assert.assertTrue(
                new HashSet<String>(Arrays.asList("E200")).equals(ocrEngine.parseResult(result8)));

        Assert.assertTrue(
                new HashSet<String>(Arrays.asList("E101a", "E200", "E1414")).equals(ocrEngine.parseResult(result9)));

//        Assert.assertTrue(
//                new HashSet<String>(Arrays.asList("E401", "E450")).equals(ocrEngine.parseResult(result10)));

    }

    /**
     * time a method's execution for RecognizeImageAsyncTaskRESTClient and RecognizeImageAsyncTaskStandalone
     * And comparing
     */
    public void testRecognizeImageAsyncTask() {

    //results of timing RecognizeImageAsyncTaskRESTClient
    //URL  41098071129  40289779933  42650368734
    //URL2 191964505082 203698485182 193867012634 67070300688 71843954948
    //URL1 123115701776 129641047762 131110172450 53560201043 42370121722 195613520967

        ArrayList<String> files = getTestImages();

        long startTime = System.nanoTime();
        for (String path : files) {

            final CountDownLatch signal = new CountDownLatch(1);
            TaskDelegateImpl delegate = new TaskDelegateImpl(signal);

            RecognizeImageAsyncTask task = new RecognizeImageAsyncTaskRESTClient(path, delegate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
            try {
                signal.await();// wait for callback
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        Log.v(TAG, "RecognizeImageAsyncTaskRESTClient: " + duration + "/n");


        startTime = System.nanoTime();
        IsolatedContext context = new IsolatedContext(new MockContentResolver(), getActivity());

        for (String path : files) {

            final CountDownLatch signal = new CountDownLatch(1);
            TaskDelegateImpl delegate = new TaskDelegateImpl(signal);

            RecognizeImageAsyncTask task = new RecognizeImageAsyncTaskStandalone(context, path, delegate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
            try {
                signal.await();// wait for callback
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        Log.v(TAG, "RecognizeImageAsyncTaskStandalone: " + duration + "/n");
    }

    private void prepareDirectories(String[] paths) {
        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path
                            + " on sdcard failed");
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }
    }

    private ArrayList<String> getTestImages() {
        //create folders for tessdata files
        prepareDirectories(
                new String[]{DATA_PATH + TEST_IMGS});

        ArrayList<String> files = new ArrayList<String>();

        try {
            AssetManager assetManager = getInstrumentation().getContext().getAssets();
            String fileList[] = assetManager.list(TEST_IMGS);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + TEST_IMGS + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = assetManager.open(TEST_IMGS + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.v(TAG, "Copied " + fileName + "to test_imgs");
                }
                files.add(pathToDataFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "Was unable to copy files to test_imgs " + e.toString());

        }
        return files;
    }

    public void appendLog(String text) {
        File logFile = new File("sdcard/log.file");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class TaskDelegateImpl implements TaskDelegate {
        private CountDownLatch signal;

        public TaskDelegateImpl(CountDownLatch signal) {
            this.signal = signal;
        }

        @Override
        public void TaskCompletionResult(String[] result) {
            StringBuilder builder = new StringBuilder();
            for (String s : result) {
                builder.append(s);
            }
            Log.v(TAG, " result: " + builder.toString());

            signal.countDown();// notify the count down latch
        }
    }
}