package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Company;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Joboffers.JobOfferStatus;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.view.RedirectView;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private JobOfferRepository jobOfferRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUpdateStatusApplication() throws Exception {
        Applications application = new Applications();
        application.setId(1);

        when(applicationRepository.findById(1)).thenReturn(java.util.Optional.of(application));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/update_applications")
                .param("applicationId", "1")
                .param("status", "ACCEPTED").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/applications"));

        verify(applicationRepository).save(application);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminPosts() throws Exception {
    	User user=new User();
    	user.setEmail("email@gmail.com");
        Joboffers jobOffer1 = new Joboffers("Job Title 1", "Description 1", 5, "Company A",
                java.time.LocalDate.now(), new Company(user,"Comapny1"), 50000, 30000,
                java.time.LocalDate.now().plusMonths(1), JobOfferStatus.PENDING);
        Joboffers jobOffer2 = new Joboffers("Job Title 2", "Description 2", 3, "Company B",
                java.time.LocalDate.now(), new Company(user,"Company2"), 60000, 35000,
                java.time.LocalDate.now().plusMonths(2), JobOfferStatus.ACCEPTED);

        when(jobOfferRepository.findAll()).thenReturn(java.util.Arrays.asList(jobOffer1, jobOffer2));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin_track_posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("joboffers"))
                .andExpect(MockMvcResultMatchers.model().attribute("joboffers", java.util.Arrays.asList(jobOffer1, jobOffer2)));

        verify(jobOfferRepository).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminApplicationsWithoutStatusFilter() throws Exception {
        List<Applications> applications = new ArrayList();
        when(applicationRepository.findAll()).thenReturn(applications);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/applications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin_track_application"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("Applications"))
                .andExpect(MockMvcResultMatchers.model().attribute("Applications", applications));

        verify(applicationRepository).findAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteApplication() throws Exception {
        Applications application = new Applications();
        application.setId(1);

        when(applicationRepository.findById(1)).thenReturn(Optional.of(application));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/deleteapplication/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/applications"));

        verify(applicationRepository).findById(1);
        verify(applicationRepository).delete(application);
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeactivateUser() throws Exception {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/deactivate_user/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testActivateUser() throws Exception {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/users/activate_user/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/users"));

        verify(userRepository).findById(1);
        verify(userRepository).save(user);
    }
    
}
