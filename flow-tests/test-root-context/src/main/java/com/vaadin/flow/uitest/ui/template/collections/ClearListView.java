package com.vaadin.flow.uitest.ui.template.collections;

import java.util.Arrays;
import java.util.List;

import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

/**
 * @author Vaadin Ltd.
 */
@Tag("clear-list")
@HtmlImport("/com/vaadin/flow/uitest/ui/template/collections/ClearList.html")
public class ClearListView extends PolymerTemplate<ClearListView.ClearListModel>
        implements View {
    public ClearListView() {
        setId("template");
        getModel().setMessages(
                Arrays.asList(new Message("1"), new Message("2")));
    }

    @EventHandler
    private void clearList() {
        getModel().getMessages().clear();
    }

    public static class Message {
        private String text;

        public Message(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public interface ClearListModel extends TemplateModel {
        void setMessages(List<Message> messages);

        List<Message> getMessages();
    }
}