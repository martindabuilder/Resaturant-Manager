package bg.tu_varna.ii.restaurantmanager.controller.page_controllers;
import bg.tu_varna.ii.restaurantmanager.model.dto.response.CategoryResponseDto;
import bg.tu_varna.ii.restaurantmanager.service.CategoryService;
import bg.tu_varna.ii.restaurantmanager.service.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationPageController {

    private final ReservationService reservationService;

    public ReservationPageController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public String reservationsPage(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservationDtos());

        return "reservations";
    }

    @Controller
    public static class CategoryPageController {

        private final CategoryService categoryService;

        public CategoryPageController(CategoryService categoryService) {
            this.categoryService = categoryService;
        }

        @GetMapping("/categories")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER', 'CHEF')")
        public String categoriesPage(@RequestParam(required = false) String name, @RequestParam(required = false) Boolean activeFilter, Model model) {

            var categories = categoryService.getAllCategories().stream()
                    .map(CategoryResponseDto::new)
                    .filter(category -> name == null || name.isBlank()
                            || category.getName().toLowerCase()
                            .contains(name.toLowerCase()))
                    .filter(category -> activeFilter == null
                            || category.isActive() == activeFilter)
                    .toList();

            model.addAttribute("categories", categories);
            model.addAttribute("name", name);
            model.addAttribute("activeFilter", activeFilter);

            return "categories";
        }

        @PostMapping("/categories/create")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
        public String createCategory(@RequestParam String name, @RequestParam(required = false) String description, RedirectAttributes redirectAttributes) {
            categoryService.createCategory(name, description);redirectAttributes.addFlashAttribute("successMessage", "Категорията беше създадена успешно.");

            return "redirect:/categories";
        }

        @PostMapping("/categories/{id}/update")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
        public String updateCategory(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String description, RedirectAttributes redirectAttributes) {
            categoryService.updateCategory(id, name, description);
            redirectAttributes.addFlashAttribute("successMessage", "Категорията беше редактирана успешно.");

            return "redirect:/categories";
        }

        @PostMapping("/categories/{id}/activate")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
        public String activateCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            categoryService.activateCategory(id);redirectAttributes.addFlashAttribute("successMessage", "Категорията беше активирана успешно.");

            return "redirect:/categories";
        }

        @PostMapping("/categories/{id}/deactivate")
        @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
        public String deactivateCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            categoryService.deactivateCategory(id);redirectAttributes.addFlashAttribute("successMessage", "Категорията беше деактивирана успешно.");

            return "redirect:/categories";
        }
    }
}
