package org.msk86.ygoroid.newaction.dueldisk.dispatcherimpl;

import org.msk86.ygoroid.newaction.Action;
import org.msk86.ygoroid.newaction.Dispatcher;
import org.msk86.ygoroid.newaction.dueldisk.actionimpl.*;
import org.msk86.ygoroid.newcore.Item;
import org.msk86.ygoroid.newcore.Selectable;
import org.msk86.ygoroid.newcore.impl.CardEffectWindow;
import org.msk86.ygoroid.newcore.impl.Coin;
import org.msk86.ygoroid.newcore.impl.Dice;
import org.msk86.ygoroid.newcore.impl.LifePoint;
import org.msk86.ygoroid.newcore.impl.lifepoint.Button;
import org.msk86.ygoroid.newcore.impl.lifepoint.LpDisplay;
import org.msk86.ygoroid.newcore.impl.lifepoint.Numbers;
import org.msk86.ygoroid.newcore.impl.lifepoint.Operator;
import org.msk86.ygoroid.newop.impl.Click;

import java.util.ArrayList;
import java.util.List;

public class ClickDispatcher implements Dispatcher<Click> {
    @Override
    public List<Action> dispatch(Click op) {
        List<Action> actionChain = new ArrayList<Action>();

        Item item = op.getItem();

        if(item instanceof LifePoint) {
            actionChain.add(new ShowLifeCalculatorAction(op));
        }
        if(item instanceof Dice) {
            actionChain.add(new ThrowDiceAction(op));
        }
        if(item instanceof Coin) {
            actionChain.add(new ThrowCoinAction(op));
        }
        if(item instanceof CardEffectWindow) {
            actionChain.add(new ShowNextPageDescAction(op));
        }
        if(item instanceof Operator) {
            actionChain.add(new ChangeLpOperatorAction(op));
        }
        if(item instanceof Numbers) {
            actionChain.add(new AppendLpNumberAction(op));
        }
        if(item instanceof LpDisplay) {
            actionChain.add(new SwitchLpDisplayAction(op));
        }
        if(item instanceof Button && item == Button.CLEAR) {
            actionChain.add(new ClearLpNumberAction(op));
        }

        return actionChain;
    }
}
