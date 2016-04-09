
package bank.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "transfer", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfer", namespace = "http://soap.bank/", propOrder = {
    "accountA",
    "accountB",
    "amount"
})
public class Transfer {

    @XmlElement(name = "accountA", namespace = "")
    private String accountA;
    @XmlElement(name = "accountB", namespace = "")
    private String accountB;
    @XmlElement(name = "amount", namespace = "")
    private double amount;

    /**
     * 
     * @return
     *     returns String
     */
    public String getAccountA() {
        return this.accountA;
    }

    /**
     * 
     * @param accountA
     *     the value for the accountA property
     */
    public void setAccountA(String accountA) {
        this.accountA = accountA;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getAccountB() {
        return this.accountB;
    }

    /**
     * 
     * @param accountB
     *     the value for the accountB property
     */
    public void setAccountB(String accountB) {
        this.accountB = accountB;
    }

    /**
     * 
     * @return
     *     returns double
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * 
     * @param amount
     *     the value for the amount property
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
