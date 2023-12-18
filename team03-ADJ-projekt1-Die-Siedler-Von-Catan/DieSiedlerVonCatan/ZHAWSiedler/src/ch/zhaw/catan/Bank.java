package ch.zhaw.catan;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all methods for the bank object and its data.
 */
public class Bank {

    private Map<Config.Resource, Integer> BankStock;

    /**
     * Constructs bank object, which contains an initial resource card stock,
     * based from the config.
     */
    public Bank() {
        BankStock = new HashMap<>(Config.INITIAL_RESOURCE_CARDS_BANK);
    }

    /**
     * This method is used to get the amount of a specific resource.
     * @param resource the resource to get the amount from.
     * @return amount of the given resource as an integer
     */
    public int getBankStockResource(Config.Resource resource) {
        return BankStock.get(resource);
    }

    /**
     * This method is used to add a specific amount of a resource to the bank stock.
     *
     * @param resource the resource, which should be added to the bank.
     * @param amount quantity of the resource.
     * @return true, if added and the final amount doesn't exceed the bank limit per resource.
     */
    public boolean addToBankStock(Config.Resource resource, int amount) {
        if (checkIfAmountIsPositive(amount)) {
            if(BankStock.get(resource) + amount <= 19){
                BankStock.replace(resource, BankStock.get(resource) + amount);
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to remove a resource from the bank stock.
     *
     * @param resource the resource to be removed.
     * @param amount the amount to be removed from the resource.
     * @return true, if the amount is a positive integer
     * AND if the amount won't set the bank to a negative state.
     */
    public boolean removeFromBankStock(Config.Resource resource, int amount) {
        if (checkIfAmountIsPositive(amount) && checkWhetherResourceCanBeRemoved(resource, amount)) {
            int remove = BankStock.get(resource) - amount;
            BankStock.put(resource, remove);
            return true;
        }
        return false;
    }

    private boolean checkIfAmountIsPositive(int number) {
        return number > 0;
    }

    private boolean checkWhetherResourceCanBeRemoved(Config.Resource resource, int number) {
        return BankStock.get(resource) - number >= 0;
    }


}
