package org.msk86.ygoroid.newcore.impl.side;

import org.msk86.ygoroid.newcore.Container;
import org.msk86.ygoroid.newcore.Item;
import org.msk86.ygoroid.newcore.Layout;
import org.msk86.ygoroid.newcore.Renderer;
import org.msk86.ygoroid.newcore.deck.DeckCards;
import org.msk86.ygoroid.newcore.impl.InfoBar;
import org.msk86.ygoroid.newcore.impl.layout.VerticalLayout;
import org.msk86.ygoroid.newcore.impl.side.renderer.SideChangerRenderer;

import java.util.ArrayList;
import java.util.List;

public class SideChanger implements Item, Container {
    DeckCards cards;
    List<Item> sections;


    public SideChanger(DeckCards cards) {
        this.cards = cards;
        sections = new ArrayList<Item>();
        sections.add(new MainDeckSection(cards));
        sections.add(new ExDeckSection(cards));
        sections.add(new SideDeckSection(cards));
        sections.add(new InfoBar(this));

    }

    public MainDeckSection getMainDeckSection() {
        return (MainDeckSection) sections.get(0);
    }
    public ExDeckSection getExDeckSection() {
        return (ExDeckSection) sections.get(1);
    }
    public SideDeckSection getSideDeckSection() {
        return (SideDeckSection) sections.get(2);
    }

    Renderer renderer;
    @Override
    public Renderer getRenderer() {
        if(renderer == null) {
            renderer  = new SideChangerRenderer(this);
        }
        return renderer;
    }

    Layout layout;
    @Override
    public Layout getLayout() {
        if( layout==null) {
            layout = new VerticalLayout(this, sections);
        }
        return layout;
    }

}
