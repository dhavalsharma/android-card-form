package com.braintreepayments.cardform.view;

import android.text.Editable;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.braintreepayments.cardform.test.Assertions.assertBitmapsEqual;
import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
public class CardEditTextTest {

    private CardEditText mView;

    @Before
    public void setup() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);
    }

    @Test
    public void testVisa() {
        helper("4", "111 1111 1111 1111", R.drawable.bt_visa, 4, 8, 12);
    }

    @Test
    public void testMasterCard() {
        helper("55", "55 5555 5555 4444", R.drawable.bt_mastercard, 4, 8, 12);
    }

    @Test
    public void testDiscover() {
        helper("6011", "1111 1111 1117", R.drawable.bt_discover, 4, 8, 12);
    }

    @Test
    public void testAmex() {
        helper("37", "82 822463 10005", R.drawable.bt_amex, 4, 10);
    }

    @Test
    public void testJcb() {
        helper("35", "30 1113 3330 0000", R.drawable.bt_jcb, 4, 8, 12);
    }

    @Test
    public void testDiners() {
        helper("3000", "0000 0000 04", R.drawable.bt_diners, 4, 8, 12);
    }

    @Test
    public void testMaestro() {
        helper("5018", "0000 0000 0000122", R.drawable.bt_maestro, 4, 8, 12);
    }

    @Test
    public void testUnionPay() {
        helper("62", "40 8888 8888 8885127", R.drawable.bt_card_highlighted, 4, 8, 12);
    }

    @Test
    public void testUnknown() {
        helper("1", "111 1111 1111 1111111", R.drawable.bt_card_highlighted, 4, 8, 12);
    }

    /* helpers */
    private void helper(String start, String end, int drawable, int... spans) {
        assertHintIs(R.drawable.bt_card_highlighted);
        type(start).assertHintIs(drawable);
        type(end).assertSpansAt(spans);
        assertCharacterMaxLengthHit();
    }

    private void assertCharacterMaxLengthHit() {
        Editable text = mView.getText();
        int maxLength = text.length();
        type("1111");
        assertEquals("Able to write more characters than max length", maxLength, text.length());
    }

    private void assertSpansAt(int... indices) {
        Editable text = mView.getText();
        List<SpaceSpan> allSpans = Arrays.asList(text.getSpans(0, text.length(),
                SpaceSpan.class));
        List<SpaceSpan> foundSpans = new ArrayList<SpaceSpan>();
        for (int i : indices) {
            SpaceSpan[] span = text.getSpans(i - 1, i, SpaceSpan.class);
            assertEquals(1, span.length);
            foundSpans.add(span[0]);
        }
        assertEquals(allSpans, foundSpans);
    }

    private void assertHintIs(int resId) {
        assertBitmapsEqual(RuntimeEnvironment.application.getResources().getDrawable(resId),
                mView.getCompoundDrawables()[2]);
    }

    private CardEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            if (c != ' ') {
                editable.append(c);
            }
        }
        return this;
    }
}
