package com.ashomok.eNumbers;


import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ashomok.eNumbers.activities.MainActivity;
import com.ashomok.eNumbers.data_load.EN_ORM;
import com.ashomok.eNumbers.ocr.OCREngine;
import com.ashomok.eNumbers.ocr.OCREngineImpl;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    private static final String TAG = "MainActivityTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, true, true);

    @Test
    public void testSQLiteAssetHelper() {
        EN_ORM en_orm = EN_ORM.getInstance(mActivityRule.getActivity());

        Assert.assertEquals(1, en_orm.getEnumbsByCodeArray(new String[]{"E100"}).size());

        Assert.assertEquals(2, en_orm.getEnumbsByCodeArray(new String[]{"E100", "E123"}).size());

        Assert.assertEquals(3, en_orm.getEnumbsByCodeArray(new String[]{"E100", "E123", "E101a"}).size());
    }


    @Test
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

        Assert.assertEquals(new HashSet<>(Arrays.asList("E202", "E211", "E950", "E951", "E954", "E955", "E621")), ocrEngine.parseResult(result1));

        Assert.assertEquals(new HashSet<>(Collections.singletonList("E200")), ocrEngine.parseResult(result2));

        Assert.assertEquals(new HashSet<String>(), ocrEngine.parseResult(result3));

        Assert.assertEquals(new HashSet<>(Arrays.asList("E1414", "E3330", "E471", "E461")), ocrEngine.parseResult(result4));

        Assert.assertEquals(new HashSet<String>(), ocrEngine.parseResult(result5));

        Assert.assertEquals(new HashSet<String>(), ocrEngine.parseResult(result6));

        Assert.assertEquals(new HashSet<String>(), ocrEngine.parseResult(result7));

        Assert.assertEquals(new HashSet<>(Collections.singletonList("E200")), ocrEngine.parseResult(result8));

        Assert.assertEquals(new HashSet<>(Arrays.asList("E101a", "E200", "E1414")), ocrEngine.parseResult(result9));

//        Assert.assertTrue(
//                new HashSet<String>(Arrays.asList("E401", "E450")).equals(ocrEngine.parseResult(result10)));

    }


}