package com.example.enumberdata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by Iuliia on 08.08.2015.
 */
public class ENumbersServiceImpl implements ENumbersService {
    private Logger log = Logger.getLogger(ENumbersServiceImpl.class.getName());

    public Collection<ENumber> getData() {
        return data;
    }

    private Collection<ENumber> data;

    public ENumbersServiceImpl(Collection<ENumber> data) {
        this.data = data;
    }

    @Override
    public void reformatAdditionalInfo() {
        extractTypicalProducts();
        extractBannedApproved();
//
//        //TODO
//        extractBadForChildrenItems(); //HACSG , ets
//        deleteDublicateInfo(); //colors ,  not permited, ets
//        addInfoInsteadSeeXXX(); //see 554 should be replaced by text
    }

    public void extractBannedIn(ENumber eNumber) {
        Collection<String> patternsBanned = new HashSet<String>(Arrays.asList(
                "banned in ",
                "not permitted in ",
                "not registered for use in "
        ));
        try {
            for (String pattern : patternsBanned) {
                String bannedIn = eNumber.getBannedIn();
                while (eNumber.getAdditionalInfo().toLowerCase().contains(pattern)) {

                    int indexPatternStart = eNumber.getAdditionalInfo().toLowerCase().indexOf(pattern);
                    int indexSubstringEnd = eNumber.getAdditionalInfo().toLowerCase().indexOf(".", indexPatternStart);

                    if (indexPatternStart < 0 || indexSubstringEnd < 0) {
                        throw new Exception("Wrong index, index < 0.");
                    }

                    if (indexPatternStart > indexSubstringEnd) {
                        throw new Exception("Wrong index, indexPatternStart > indexSubstringEnd");
                    }

                    String substring = eNumber.getAdditionalInfo().substring(
                            indexPatternStart + pattern.length(), //after pattern
                            indexSubstringEnd); //before "."

                    int allWordsAmount = getAllWordsCount(substring);
                    int amountOfCountries = getAmountOfCapitalLetters(substring);

                    if (allWordsAmount > 0 &&
                            30 < 100 * amountOfCountries / allWordsAmount) { //words with name of country more than 30%
                        if (substring.length() > bannedIn.length())
                        {

                            bannedIn = substring;
                            eNumber.setBannedIn(bannedIn);
                        }

                        String patternWithSubstring = eNumber.getAdditionalInfo().substring(
                                indexPatternStart, //from pattern
                                indexSubstringEnd + 1); //with "."

                        //cut bannedin info from AdditionalInfo
                        eNumber.setAdditionalInfo(eNumber.getAdditionalInfo().replace(patternWithSubstring, ""));
                    }
                    else
                    {
                        //don't cut bannedin info from AdditionalInfo
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAllWordsCount(String substring) {
        if (substring.isEmpty())
            return 0;
        return substring.split("\\W+").length;
    }

    private int getAmountOfCapitalLetters(String substring) {
        int result = 0;
        if (substring.isEmpty()) {
            return 0;
        } else {
            String[] words = substring.split("\\W+");
            for (String word : words) {
                if (Character.isUpperCase(word.charAt(0))) {
                    result++;
                }
            }
        }
        return result;
    }

    //TODO ??? add "used in"
    public void extractTypicalProducts(ENumber eNumber) {
        try {
            Collection<String> patterns = new HashSet<String>(Arrays.asList(
                    "typical products include ",
                    "typical products are ",
                    "typically used in "
            ));

            for (String pattern : patterns) {
                String typicalProducts = eNumber.getTypicalProducts();
                while (eNumber.getAdditionalInfo().toLowerCase().contains(pattern)) {

                    int indexPatternStart = eNumber.getAdditionalInfo().toLowerCase().indexOf(pattern);
                    int indexSubstringEnd = eNumber.getAdditionalInfo().toLowerCase().indexOf(".", indexPatternStart);

                    if (indexPatternStart < 0 || indexSubstringEnd < 0) {
                        throw new Exception("Wrong index, index < 0.");
                    }

                    if (indexPatternStart > indexSubstringEnd) {
                        throw new Exception("Wrong index, indexPatternStart > indexSubstringEnd");
                    }

                    String substring = eNumber.getAdditionalInfo().substring(
                            indexPatternStart + pattern.length(), //after pattern
                            indexSubstringEnd); //before "."

                    if (substring.length() > typicalProducts.length()) {

                        typicalProducts = substring;
                        eNumber.setTypicalProducts(typicalProducts);
                    }

                    String patternWithSubstring = eNumber.getAdditionalInfo().substring(
                            indexPatternStart, //from pattern
                            indexSubstringEnd + 1); //with "."

                    //cut typical products from AdditionalInfo
                    eNumber.setAdditionalInfo(eNumber.getAdditionalInfo().replace(patternWithSubstring, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void extractTypicalProducts() {

        for (ENumber item : data) {
            extractTypicalProducts(item);
            log.info(item.getCode() + " TypicalProducts extracted");
        }
    }

    @Override
    public void extractBannedApproved() {

        for (ENumber item : data) {

            extractBannedIn(item);
            log.info(item.getCode() + " BannedIn extracted");

            extractApprovedIn(item);
            log.info(item.getCode() + " ApprovedIn extracted");
        }
    }

    private void extractApprovedIn(ENumber item) {
//1. extract from status

    }


    private void addInfoInsteadSeeXXX() {
        //  , "Typical products see" //TODO
    }

    private void deleteDublicateInfo() {

    }

    private void extractBadForChildrenItems() {
    }


}
