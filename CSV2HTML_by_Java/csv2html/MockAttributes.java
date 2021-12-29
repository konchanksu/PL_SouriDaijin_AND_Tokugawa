package csv2html;

/**
 * Attributesの具象クラス
 * 
 * @author Kajiwara Ryutaro
 * @version 1.0.0
 */
public class MockAttributes extends Attributes{
    @Override
    String captionString(){
        return "";
    }
    String baseDirectory(){
        return "";
    }
    String baseUrl(){
        return "";
    }
    String csvUrl(){
        return "";
    }
    String titleString(){
        return "";
    }
}