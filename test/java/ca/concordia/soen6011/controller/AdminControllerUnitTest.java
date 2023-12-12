package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.*;
import ca.concordia.soen6011.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerUnitTest {

    @InjectMocks
    private AdminController controller;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        // Set up any common mock behavior here if required
    }

    @Test
    void testAdminUpdateStatusApplication() {
        Applications application = new Applications();
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.of(application));

        RedirectView result = controller.adminUpdateStatusApplication(1, "ACCEPTED");
        assertEquals("/admin/applications", result.getUrl());
        verify(applicationRepository).save(application);
        assertEquals(Applications.ApplicationStatus.ACCEPTED, application.getStatus());
    }

    @Test
    void testAdminPosts() {
        List<Joboffers> jobOffers = Arrays.asList(new Joboffers());
        when(jobOfferRepository.findAll()).thenReturn(jobOffers);

        String result = controller.adminPosts(model, authentication);

        assertEquals("admin_track_posts", result);
        verify(model).addAttribute("joboffers", jobOffers);
    }

    @Test
    void testAdminApplications() {
        List<Applications> applications = Arrays.asList(new Applications());
        when(applicationRepository.findAll()).thenReturn(applications);

        String result = controller.adminApplications(model, null);

        assertEquals("admin_track_application", result);
        verify(model).addAttribute("Applications", applications);
    }

    @Test
    void testDeleteApplication() {
        Applications application = new Applications();
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.of(application));

        RedirectView result = controller.deleteApplication("1");

        assertEquals("/admin/applications", result.getUrl());
        verify(applicationRepository).delete(application);
    }

    @Test
    void testAdminFilterApplications() {
        List<Applications> applications = Arrays.asList(new Applications());
        when(applicationRepository.findAllByStatus(Applications.ApplicationStatus.ACCEPTED)).thenReturn(applications);

        String result = controller.adminFilterApplications("ACCEPTED", model);

        assertEquals("admin_filter_application", result);
        verify(model).addAttribute("Applications", applications);
    }

    @Test
    void testAdminUpdateJobOfferStatus() {
        Joboffers jobOffer = new Joboffers();
        when(jobOfferRepository.findById(anyInt())).thenReturn(Optional.of(jobOffer));

        RedirectView result = controller.adminUpdateJobofferStatus(1, "ACCEPTED");

        assertEquals("/admin/posts", result.getUrl());
        verify(jobOfferRepository).save(jobOffer);
        assertEquals(Joboffers.JobOfferStatus.ACCEPTED, jobOffer.getStatus());
    }

    @Test
    void testAdminFilterStatus() {
        RedirectView result = controller.adminFilterStatus("ACCEPTED");

        assertEquals("/admin/filterapplication/ACCEPTED", result.getUrl());
    }

    @Test
    void testFilterPageAdmin() {
        List<Joboffers> jobOffers = Arrays.asList(new Joboffers());
        when(jobOfferRepository.findAllByStatus(Joboffers.JobOfferStatus.ACCEPTED)).thenReturn(jobOffers);

        String result = controller.filterPageAdmin("ACCEPTED", model);

        assertEquals("admin_filter_joboffers", result);
        verify(model).addAttribute("joboffers", jobOffers);
    }

    @Test
    void testSearchJobs() {
        List<Joboffers> jobOffers = Arrays.asList(new Joboffers());
        when(jobOfferRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(jobOffers);

        String result = controller.searchJobs("test", model);

        assertEquals("search_joboffer_admin", result);
        verify(model).addAttribute("joboffers", jobOffers);
    }

//    @Test
//    void testManageUsers() {
//        List<User> users = Arrays.asList(new User());
//        when(userRepository.findAllExceptUsername(anyString())).thenReturn(users);
//
//        String result = controller.manageUsers(model, authentication);
//
//        assertEquals("admin_manage_users", result);
//        verify(model).addAttribute("users", users);
//    }

    @Test
    void testDeactivateUser() {
        User user = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        RedirectView result = controller.deactivateUser("1");

        assertEquals("/admin/users", result.getUrl());
        verify(userRepository).save(user);
        assertEquals(false, user.getIsActive());
    }

    @Test
    void testActivateUser() {
        User user = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        RedirectView result = controller.activateUser("1");

        assertEquals("/admin/users", result.getUrl());
        verify(userRepository).save(user);
        assertEquals(true, user.getIsActive());
    }
}
