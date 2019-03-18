package click.acme.genius.utils;

/**
 * Pour transformer la référence en String et inversement
 */
public class ScholarLevel {

    public static String FromIntToString(int value){
        switch (value){
            case 0:
                return "6éme";
            case 1:
                return "5éme";
            case 2:
                return "4éme";
            case 3:
                return "3éme";
            case 4:
                return "2nde";
            case 5:
                return "1ère";
            case 6:
                return "Terminal";
            case 7:
                return "Prepa 1";
            case 8:
                return "Prepa 2";
            case 9:
                return "Prepa 3";
            case 10:
                return "Licence";
            case 11:
                return "Master";
            case 12:
            default:
                return "Autres";
        }

    }

    public static int FromStringToInt(String value){
        switch (value){
            case "6éme":
                return 0;
            case "5éme":
                return 1;
            case "4éme":
                return 2;
            case "3éme":
                return 3;
            case "2nde":
                return 4;
            case "1ère":
                return 5;
            case "Terminal":
                return 6;
            case "Prepa 1":
                return 7;
            case "Prepa 2":
                return 8;
            case "Prepa 3":
                return 9;
            case "Licence":
                return 10;
            case "Master":
                return 11;
            case "Autres":
            default:
                return 12;
        }

    }

}