package rf.protocols.external.ognl;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * @author Eugene Schava <eschava@gmail.com>
 */
public class OgnlMessageFormatter {

    public static String format(String template, Object root) {
        StringBuilder buf = new StringBuilder();
        int                       currentIndex = 0;
        int                       lastIndex = 0;

        if ( template == null )
        {
            return "";
        }

        currentIndex = template.indexOf( "${" );
        while ( currentIndex != -1 )
        {
            String                  ognlExp;

            if ( currentIndex > lastIndex )
            {
                buf.append( template.substring( lastIndex, currentIndex ) );
            }

            lastIndex = template.indexOf( '}', currentIndex );
            if ( lastIndex == -1 )
            {
                buf.append( template.substring( currentIndex ) );

                break;
            }

            ognlExp = template.substring( currentIndex + 2, lastIndex );

            try
            {
                //Process the OGNL expression.
                buf.append( Ognl.getValue(ognlExp, root) );
            }
            catch ( OgnlException e )
            {
                throw new RuntimeException( "Error executing OGNL expression [" +
                        ognlExp +
                        "] within template [" +
                        template + "].",
                        e );
            }

            lastIndex++;
            currentIndex = template.indexOf( "${", lastIndex );
        }

        //Grab the last part of the template, if there is any.
        if ( lastIndex < template.length() && lastIndex != -1 )
        {
            buf.append( template.substring( lastIndex ) );
        }

        return buf.toString();
    }
}
