package com.example.enumberdata;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Iuliia on 08.08.2015.
 */

@Test
public class ENumbersServiceTest {

    private ArrayList<ENumber> eNumbers;
    private ENumbersServiceImpl eNumberService;
    private Logger log = Logger.getLogger(ENumbersServiceTest.class.getName());

    @BeforeClass
    public void init() {
        eNumbers = new ArrayList<ENumber>();
        ENumber eNumberE100 = new ENumber(
                "E100",
                "Curcumin (from turmeric)",
                "Color Yellow-orange",
                "Approved in the EU.");
        eNumberE100.AddAdditionalInfo("Animal genes damage, possible risk to conception and cancer. " +
                "JECFA still evaluating re reproductive toxicity. Orange-yellow colour; derived from " +
                "the root of the curcuma plant, but can be artificially produced; used in cheese, " +
                "margarine, baked sweets and fish fingers It has beneficial effect on the blood sugar " +
                "in diabetics. It can increase the liver's secretion of bile and protect the liver from " +
                "toxic substances. Curcumin may be used to compensate for fading of natural colouring " +
                "in pre-packed foods. Recognised as an anti-carcinogenic agent during laboratory tests. " +
                "Typical products include fish fingers, fizzy drinks, butter and other dairy produce," +
                " cakes and biscuits, margarine, processed cheese, curry powder, cooking oil, sweets," +
                " cereals and sauces. Orange-yellow color, derived from turmeric, a member of the ginger" +
                " family.");

        ENumber eNumberE102 = new ENumber(
                "E102",
                "Tartrazine (FD&amp;C Yellow 5)",
                "Color Lemon yellow",
                "Approved in the EU.");
        eNumberE102.AddAdditionalInfo("Linked to hyperactivity, skin rashes, migraines, behavioural probs, thyroid probs, " +
                "chromosome damage. Banned in Norway and Austria. FD&amp;C Yellow No:5; CI Acid Yellow23, CI Food Yellow 4. " +
                "Coal tar dye. Polycyclic Aromatic Hydrocarbon. Cancer probability. Known to provoke asthma attacks (though " +
                "the US FDA** do not recognise this) and urticaria (nettle rash) in children (the US FDA** estimates 1:10 000)," +
                " altered states of perception and behaviour, uncontrolled hyper agitation and confusion; wakefulness in" +
                " young children. Is known to inhibit zinc metabolism and interfere with digestive enzymes. Tartrazine " +
                "sensitivity is also linked to aspirin sensitivity; used to colour drinks, sweets, jams, cereals, snack " +
                "foods, canned fish, packaged soups and a dye for wool and silk.. Banned in Norway, Austria and Finland." +
                " Restricted use in Sweden and Germany. Yellow color, commonly used, the HACSG* recommends to avoid it. " +
                "Typical products are soft drinks, confectionary, cordials, pickles. ");

        ENumber eNumberE220 = new ENumber(
                "E220",
                "Sulphur dioxide",
                "preservative",
                "Approved in the EU.");
        eNumberE220.AddAdditionalInfo("Asthmatics should avoid, gastric irritation / damage, hyperactivity, " +
                "behavioural problems, poss mutagen. Can be fatal to asthmatics. Preservative. Occurs naturally " +
                "in the atmosphere and as a pollutant gas from combustion processes, sulphur dioxide is implicated" +
                " in formation of acid rain and has a choking odour. Derived from coal tar; all sulphur drugs are " +
                "toxic and restricted in use (in USA, FDA** prohibits their use on raw fruits and vegetables), " +
                "produced by combustion of sulphur, hydrogen sulphide or gypsum; known to provoke gastric irritation, " +
                "nausea, diarrhoea, skin rash, asthma attacks and difficult to metabolise for those with impaired " +
                "kidney function, also destroys vitamin B1 (thiamin), and should be avoided by anyone suffering from" +
                " conjunctivitis, bronchitis, emphysema, bronchial asthma, or cardiovascular disease. Typical products" +
                " are beer, soft drinks, dried fruit, juices, cordials, wine, vinegar, potato products. Similar " +
                "functional properties are displayed by the sulphites (E221-E227). Other names: sulphur " +
                "superoxide. The HACSG* recommends to avoid it. May cause allergic reactions in asthmatics," +
                " destroys vitamin B1, typical products are beer, soft drinks, dried fruit, juices, cordials, " +
                "wine, vinegar, potato products. ");

        ENumber eNumberE332 = new ENumber(
                "E332",
                "Potassium citrates (i) Monopotassium citrate (ii) Potassium citrate (tripotassium citrate)",
                "acidity regulator",
                "Approved in the EU.");
        eNumberE332.AddAdditionalInfo("(i) Monopotassium citrate is the potassium salt of citric acid, E330, and is " +
                "used as an antioxidant in food as well as to improve the effects of other antioxidants. It is also " +
                "used as an acidity regulator and sequestrant. Typical products include gelatine products, jam, " +
                "sweets, ice cream, carbonated beverages, milk powder, wine, processed cheeses.  (ii) Tripotassium " +
                "citrate  is the potassium salt of citric acid, E330, and is used as an antioxidant in food as well " +
                "as to improve the effects of other antioxidants. It is also used as an acidity regulator and " +
                "sequestrant. Typical products include gelatine products, jam, sweets, ice cream, carbonated " +
                "beverages, milk powder, wine, processed cheeses. No known adverse effects. ");

        ENumber eNumberE924 = new ENumber(
                "E924",
                "Potassium bromate",
                "improving agent",
                "Approved in the EU.");
        eNumberE924.AddAdditionalInfo("Large quantities can cause nausea, vomiting, diarrhoea, abdominal pain, kidney " +
                "damage and failure. The WHO in 1993 said that this ingredient is no longer acceptable for use as it is" +
                " a possible carcinogen.  Typically used in flour products. Large quantities can cause nausea, vomiting," +
                " diarrhoea, pain. Typical products are flour products. ");


        eNumbers.add(eNumberE100);
        eNumbers.add(eNumberE102);
        eNumbers.add(eNumberE220);
        eNumbers.add(eNumberE332);
        eNumbers.add(eNumberE924);
        eNumberService = new ENumbersServiceImpl(eNumbers);
    }

    @Test
    public void testExtractTypicalProducts() {
        //TODO add more checks
        eNumberService.extractTypicalProducts();
        for (ENumber item : eNumberService.getData()) {
            if (!item.getTypicalProducts().isEmpty())
            {
                assert (!item.getAdditionalInfo().contains(item.getTypicalProducts()));
            }
//            log.info(item.getAdditionalInfo());
//            log.info(item.getTypicalProducts());
        }
    }

    @Test
    public void testExtractBannedApproved()
    {
        eNumberService.extractBannedApproved();
        for (ENumber item : eNumbers) {
            if (!item.getBannedIn().isEmpty());
            {
//                assert (!item.getAdditionalInfo().contains(item.getBannedIn()));
            }
            log.info(item.getAdditionalInfo());
            log.info(item.getBannedIn());
        }
    }
}
