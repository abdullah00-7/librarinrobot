package sa.edu.sa.librarian.robot;

import java.io.UnsupportedEncodingException;

public class Packet
{

    private int sq; // the sequence number
    private int command; // the command
    private int chksum; // the chkSum for the message
    private String[] parameter; // the parameter list
    private String strDataFrame; // the dataFrame in String Format
    private byte[] byteDataFrame; // the byte Data Frame
    // set whether packet going to discard
    /**
     * whether the packet is corrupt
     */
    public boolean isDiscard = true;
    // char set
    private static final String CHARSET = "US-ASCII";

    /**
     * The constructor to Create the packet by the input parameters
     *
     * @param sq
     *            the Sequence number
     * @param command
     *            the command number
     * @param parameter
     *            the parameter
     */
    public Packet(int sq, int command, String[] parameter)
    {
        this.isDiscard = false;
        /**
         * create the parameter while prameter is null
         */
        if (parameter == null)
        {
            parameter = new String[0];
        }
        this.sq = sq;
        this.command = command;
        this.parameter = parameter;
        this.strDataFrame = "<";
        this.strDataFrame += String.valueOf(this.sq);// the start
        this.strDataFrame += "|" + this.command;// the command
        this.strDataFrame += "|" + this.parameter.length;// length
        this.strDataFrame += "|";
        // add the parameters
        for (String s : parameter)
        {
            this.strDataFrame += (s + "|");
        }

        // compute the checksum
        this.chksum = 0;
        for (int i = 0; i < this.strDataFrame.length(); i++)
        {
            this.chksum += this.strDataFrame.charAt(i);
        }

        // set the checkSum
        this.strDataFrame += String.valueOf(this.chksum) + ">";
        try
        {
            this.byteDataFrame = this.encode(strDataFrame);
        }
        catch (UnsupportedEncodingException ex)
        {

            this.isDiscard = true;
        }
    }

    /**
     *  use the byte to construct a packet
     * @param byteDateFrame
     */
    public Packet(byte[] byteDateFrame)
    {
        this.isDiscard = true;
        this.byteDataFrame = byteDateFrame;

        // covert the byte to String
        try
        {
            this.strDataFrame = this.decode(this.byteDataFrame);
        }
        catch (UnsupportedEncodingException e)
        {
            this.isDiscard = true;
        }
        // check the start and end
        if (!this.chkRevData(strDataFrame))
        {
            this.isDiscard = true;
        }

        String strData = this.strDataFrame.substring(1, this.strDataFrame.length() - 1);

        int len = 0;
        for (int i = 0; i < strData.length(); i++)
        {
            if (strData.charAt(i) == '|')
            {
                len++;
            }
        }
        // get the length
        len += 1;
        String[] elements = new String[len];
        int beginIdx = 0;
        int endIdx = -1;

        for (int i = 0; i < len; i++)
        {
            beginIdx = endIdx + 1;
            endIdx = strData.indexOf("|", beginIdx);

            if (endIdx != -1)
            {
                elements[i] = strData.substring(beginIdx, endIdx);

            }
            else
            {
                elements[i] = strData.substring(beginIdx, strData.length());
            }
        }

        int tempChkSum = Integer.valueOf(elements[elements.length - 1]);

        String tempData = "<";

        for (int i = 0; i < elements.length - 1; i++)
        {
            tempData += elements[i] + "|";
        }
        // compute the check sum
        if (!chkSum(tempData, tempChkSum))
        {
            this.isDiscard = true;
        }

        // put the ChkSum
        this.chksum = tempChkSum;

        // put the elements
        this.sq = Integer.valueOf(elements[0]);
        this.command = Integer.valueOf(elements[1]);
        // the paraLen
        int paraLen = Integer.valueOf(elements[2]);
        String[] para = new String[paraLen];
        // parameter
        for (int i = 0; i < para.length; i++)
        {
            para[i] = elements[3 + i];
        }

        this.parameter = para;
        this.isDiscard = false;
    }

    /**
     * validate the start,end flag
     * @param dateFrm
     * @return true for validated.
     */
    private boolean chkRevData(String dateFrm)
    {
        // check the start
        if (dateFrm.charAt(0) != '<')
        {
            // System.out.println("Wrong Start flg");
            this.isDiscard = true;
            return false;
        }
        // check the end
        if (dateFrm.charAt(dateFrm.length() - 1) != '>')
        {
            //System.out.println("End Start flg");
            this.isDiscard = true;
            return false;
        }
        return true;
    }

    /**
     * compute and compare the checksum
     * @param strDataFrame
     * @param chkSum
     * @return true for validate
     */
    private boolean chkSum(String strDataFrame, int chkSum)
    {

        int tempChkSum = 0;
        for (int i = 0; i < strDataFrame.length(); i++)
        {
            tempChkSum += strDataFrame.charAt(i);
        }
        return ((chkSum == tempChkSum) ? true : false);
    }

    /**
     * Encode the String to byte
     *
     * @param strPlainText
     * @return the encode byte[]
     * @throws UnsupportedEncodingException
     */
    private byte[] encode(String strPlainText) throws UnsupportedEncodingException
    {

        byte[] ascii = strPlainText.getBytes(CHARSET);
        byte[] ascTemp = new byte[ascii.length + 1];
        System.arraycopy(ascii, 0, ascTemp, 0, ascii.length);
        System.arraycopy(new byte[]
                {
                    0x00
                }, 0, ascTemp, ascTemp.length - 1, 1);
        return ascTemp;
    }

    /**
     * Convert an ASCII byte array to a string
     *
     * @param bytes
     *            the byte array
     * @return the String
     * @throws UnsupportedEncodingException
     */
    private String decode(final byte[] bytes) throws UnsupportedEncodingException
    {
        byte lastByte = bytes[bytes.length - 1];
        if (lastByte != 0x00)
        {
            this.isDiscard = true;
            throw new UnsupportedEncodingException("The String must be Zero at the last bit");
        }

        byte[] ascii = new byte[bytes.length - 1];
        System.arraycopy(bytes, 0, ascii, 0, bytes.length - 1);
        return new String(ascii, CHARSET);
    }

    /**
     *  get the Sequence number
     * @return seq
     */
    public int getSequenceNumber()
    {
        return sq;
    }

    /**
     * return the command number
     * @return command number
     */
    public int getCommand()
    {
        return command;
    }

    /**
     * return the check sum
     * @return check sum
     */
    public int getChksum()
    {
        return chksum;
    }

    /**
     * return the parameters
     * @return parameter string array
     */
    public String[] getParameter()
    {
        return parameter;
    }

    /**
     * the data in plain text
     * @return string format of the plain text
     */
    public String getDataFrameInString()
    {
        return strDataFrame;
    }

    /**
     *  get the bytes for sending
     * @return the bytes for sending
     */
    public byte[] getDataFrameInByte()
    {
        return byteDataFrame;
    }
}
