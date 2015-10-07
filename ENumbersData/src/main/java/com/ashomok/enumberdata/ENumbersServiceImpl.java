package com.ashomok.enumberdata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
        deleteDuplicateInfo();
        extractBadForChildrenItems();
        extractAvoidItItems();
        orderPunctuation();
        addDangerLevel();
        refreshBadForChildren();
    }

    private void refreshBadForChildren() {
        for (ENumber item : data) {
            refreshBadForChildren(item);
        }
    }

    private void refreshBadForChildren(ENumber item) {
        ENumber.DangerLevel dangerLevel = item.getDangerLevel();
        switch (dangerLevel) {
            case safe:
                break;
            case medium:
                item.setBadForChildren(true);
                break;
            case hight:
                item.setBadForChildren(true);
                break;
            case unknown:
                break;
            default:
                break;
        }
    }

    private void addDangerLevel() {
        for (ENumber item : data) {
            addDangerLevel(item);
        }
    }

    private void addDangerLevel(ENumber item) {
        if (item.getAvoidIt() ||
                (item.getBannedIn().length() > 0 && item.getApprovedIn().length() == 0)) {
            item.setDangerLevel(ENumber.DangerLevel.hight);
        } else if (item.getBadForChildren() || item.getBannedIn().length() > item.getApprovedIn().length()) {
            item.setDangerLevel(ENumber.DangerLevel.medium);
        } else if (item.getBannedIn().length() == 0 && item.getApprovedIn().length() > 0) {
            item.setDangerLevel(ENumber.DangerLevel.safe);
        } else {
            item.setDangerLevel(ENumber.DangerLevel.unknown);
        }

    }

    @Override
    public void extractTypicalProducts() {

        for (ENumber item : data) {
            extractTypicalProducts(item);
//            log.info(item.getCode() + " TypicalProducts extracted");
        }
    }

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

                        typicalProducts = Character.toUpperCase(substring.charAt(0)) + substring.substring(1);
                        eNumber.setTypicalProducts(typicalProducts);
                    }

                    String patternWithSubstring = eNumber.getAdditionalInfo().substring(
                            indexPatternStart, //from pattern
                            indexSubstringEnd + 1); //with "."

                    //cut typical products from AdditionalInfo
                    eNumber.setAdditionalInfo(eNumber.getAdditionalInfo().replace(patternWithSubstring, "."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void extractBannedApproved() {

        for (ENumber item : data) {

            extractBannedIn(item);
//            log.info(item.getCode() + " BannedIn extracted");

            extractApprovedIn(item);
//            log.info(item.getCode() + " ApprovedIn extracted");
        }
    }

    public void extractBannedIn(ENumber eNumber) {
        Collection<String> patternsBanned = new HashSet<String>(Arrays.asList(
                "banned in ",
                "not permitted in ",
                "not registered for use in ",
                "not permitted in "
        ));
        try {
            for (String pattern : patternsBanned) {
                String bannedIn = eNumber.getBannedIn();
                while (eNumber.getAdditionalInfo().toLowerCase().contains(pattern)) {

                    int indexPatternStart = eNumber.getAdditionalInfo().toLowerCase().indexOf(pattern);
                    int indexSubstringEnd = eNumber.getAdditionalInfo().toLowerCase().indexOf(".", indexPatternStart);

                    if (indexPatternStart < 0 || indexSubstringEnd < 0) {
                        throw new Exception("Wrong index, index < 0."); //error here
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
                        if (substring.length() > bannedIn.length()) {

                            bannedIn = substring;
                            eNumber.setBannedIn(bannedIn);
                        }

                        String patternWithSubstring = eNumber.getAdditionalInfo().substring(
                                indexPatternStart, //from pattern
                                indexSubstringEnd + 1); //with "."

                        //cut bannedin info from AdditionalInfo
                        eNumber.setAdditionalInfo(eNumber.getAdditionalInfo().replace(patternWithSubstring, "."));
                    } else {
                        //don't cut bannedin info from AdditionalInfo
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO may be one universal method with extractBannedIn see http://tutorials.jenkov.com/java-reflection/fields.html
    private void extractApprovedIn(ENumber eNumber) {
//extract from status
        try {
            Collection<String> patterns = new HashSet<String>(Arrays.asList(
                    "approved in "
            ));

            for (String pattern : patterns) {
                String approvedIn = eNumber.getApprovedIn();
                while (eNumber.getStatus().toLowerCase().contains(pattern)) {

                    int indexPatternStart = eNumber.getStatus().toLowerCase().indexOf(pattern);
                    int indexSubstringEnd = eNumber.getStatus().toLowerCase().indexOf(".", indexPatternStart);

                    if (indexPatternStart < 0 || indexSubstringEnd < 0) {
                        throw new Exception("Wrong index, index < 0. for " + eNumber.getCode() + " with status = " + eNumber.getStatus());
                    }

                    if (indexPatternStart > indexSubstringEnd) {
                        throw new Exception("Wrong index, indexPatternStart > indexSubstringEnd");
                    }

                    String substring = eNumber.getStatus().substring(
                            indexPatternStart + pattern.length(), //after pattern
                            indexSubstringEnd); //before "."

                    int allWordsAmount = getAllWordsCount(substring);
                    int amountOfCountries = getAmountOfCapitalLetters(substring);

                    if (allWordsAmount > 0 &&
                            30 < 100 * amountOfCountries / allWordsAmount) { //words with name of country more than 30%
                        if (substring.length() > approvedIn.length()) {

                            String patternThe = "[Tt]he "; //"the EU" will be "EU"
                            approvedIn = Pattern.compile(patternThe).matcher(substring).replaceAll("");
                            eNumber.setApprovedIn(approvedIn);
                        }

                        String patternWithSubstring = eNumber.getStatus().substring(
                                indexPatternStart, //from pattern
                                indexSubstringEnd + 1); //with "."

                        //cut approvedIn info from Status
                        eNumber.setStatus(eNumber.getStatus().replace(patternWithSubstring, "."));
                    } else {
                        //don't cut approvedIn info from Status
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private void deleteDuplicateInfo() {
        for (ENumber item : data) {

            deleteDuplicateInfo(item);
        }
    }

    private void deleteDuplicateInfo(ENumber eNumber) {
        Collection<String> patterns = new HashSet<String>(Arrays.asList(
                "[a-zA-Z]+\\s+colou*r,\\s",  //delete info about color
                "[Ss]ee [^.]*[.?!]"  //delete see Exxx
        ));
        for (String pattern : patterns) {

            eNumber.setAdditionalInfo(
                    Pattern.compile(pattern).matcher(eNumber.getAdditionalInfo()).replaceAll("."));
        }

        String patternNotKnownEffects = "[Nn]o known adverse effects";
        eNumber.setAdditionalInfo(
                Pattern.compile(patternNotKnownEffects).matcher(eNumber.getAdditionalInfo()).replaceFirst("."));
    }

    private void extractBadForChildrenItems() {
        for (ENumber item : data) {

            extractBadForChildrenItems(item);
        }
    }

    private void extractBadForChildrenItems(ENumber eNumber) {

        //delete info "the HACSG* recommends to avoid it" and set badForChildren = true;
        String regex = "[tT]he HACSG\\* recommends to avoid it";

        if (Pattern.compile(regex).matcher(eNumber.getAdditionalInfo()).find()) {
            eNumber.setAdditionalInfo(
                    Pattern.compile(regex).matcher(eNumber.getAdditionalInfo()).replaceAll("."));
            eNumber.setBadForChildren(true);
        }
    }

    private void extractAvoidItItems() {
        for (ENumber item : data) {

            extractAvoidItItems(item);
        }
    }

    private void extractAvoidItItems(ENumber eNumber) {
        //delete info "avoid it" and set avoidIt = true;
        String regex = "[Aa]void it";

        if (Pattern.compile(regex).matcher(eNumber.getAdditionalInfo()).find()) {
            eNumber.setAdditionalInfo(
                    Pattern.compile("[\\.,].{0,20}[Aa]void it").matcher(eNumber.getAdditionalInfo()).replaceAll("."));
            eNumber.setAvoidIt(true);
        }
    }

    private void orderPunctuation() {
        for (ENumber item : data) {

            orderPunctuation(item);
        }
    }

    private void orderPunctuation(ENumber eNumber) {

        String patternChars = "[^\\w]+\\.";  //any chars with . at the end, example !!!.
        eNumber.setAdditionalInfo(
                Pattern.compile(patternChars).matcher(eNumber.getAdditionalInfo()).replaceAll("."));

        String patternSpaces = "[\\s]{2,}";  //two or more spaces
        eNumber.setAdditionalInfo(
                Pattern.compile(patternSpaces).matcher(eNumber.getAdditionalInfo()).replaceAll(" "));

        String dotInTheStart = "^\\.[\\s]*";
        eNumber.setStatus(
                Pattern.compile(dotInTheStart).matcher(eNumber.getStatus()).replaceAll(""));

        eNumber.setAdditionalInfo(
                Pattern.compile(dotInTheStart).matcher(eNumber.getAdditionalInfo()).replaceAll(""));

        String notEnglishChars = "[^\\x00-\\x7F]+";
        eNumber.setAdditionalInfo(
                Pattern.compile(notEnglishChars).matcher(eNumber.getAdditionalInfo()).replaceAll(""));

        String spaceBeforePunctuation = "[\\s]+,";
        eNumber.setAdditionalInfo(
                Pattern.compile(spaceBeforePunctuation).matcher(eNumber.getAdditionalInfo()).replaceAll(","));

        String spaceBeforePunctuation2 = "[\\s]+;";
        eNumber.setAdditionalInfo(
                Pattern.compile(spaceBeforePunctuation2).matcher(eNumber.getAdditionalInfo()).replaceAll(";"));

        String spaceAtTheEndOfString = "[\\s]$";
        eNumber.setAdditionalInfo(
                Pattern.compile(spaceAtTheEndOfString).matcher(eNumber.getAdditionalInfo()).replaceAll(""));
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
}
