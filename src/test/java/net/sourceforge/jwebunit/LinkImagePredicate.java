package net.sourceforge.jwebunit;

import com.meterware.httpunit.HTMLElementPredicate;
import com.meterware.httpunit.WebLink;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This is a patch for JWebUnit -- we were having problems with the IMG tag case sensitivity.
 */
public class LinkImagePredicate implements HTMLElementPredicate {

    public LinkImagePredicate() {
    }

    public boolean matchesCriteria(Object webLink, Object criteria) {
        WebLink link = (WebLink) webLink;
        Element a    = (Element) link.getDOMSubtree();
        Element img  = getChildImageElement(a);
        if (img == null) {
            return false;
        }else {
            String src = img.getAttribute("src");
            String suffix = (String) criteria;
            return src.endsWith(suffix) || src.endsWith(suffix + "\"");
            //JM 09/20/04: somehow httpunit sometimes does not parse out the " surrounding attributes
        }
    }

    private Element getChildImageElement(Element htmlElement) {
        NodeList nodes = htmlElement.getElementsByTagName("IMG");
        return nodes.getLength() != 0 ? (Element) nodes.item(0) : null;
    }
}
