package ca.concordia.soen6011.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
class HomeControllerUnitTest {

	@InjectMocks
	private HomeController controller;

    @Test
    void testIndex() {
    	
        String result = controller.getIndexView();
        assertEquals("index", result);
    }

    @Test
    void testError() {
    	
        String result = controller.getErrorView();
        assertEquals("error", result);
    }
}
