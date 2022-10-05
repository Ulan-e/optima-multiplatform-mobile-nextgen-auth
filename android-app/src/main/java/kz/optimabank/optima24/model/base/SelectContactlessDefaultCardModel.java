package kz.optimabank.optima24.model.base;
import kz.optimabank.optima24.db.entry.DigitizedCard;
import kz.optimabank.optima24.model.gson.response.UserAccounts;

public class SelectContactlessDefaultCardModel {
    private UserAccounts.Cards card;
    private DigitizedCard digitizedCard;
    private boolean isSelected;

    public SelectContactlessDefaultCardModel(UserAccounts.Cards card, boolean isSelected) {
        this.card = card;
        this.isSelected = isSelected;
    }

    public SelectContactlessDefaultCardModel(DigitizedCard card, boolean isSelected) {
        this.digitizedCard = card;
        this.isSelected = isSelected;
    }

    public DigitizedCard getDigitizedCard() {
        return digitizedCard;
    }

    public UserAccounts.Cards getCard() {
        return card;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
