package edu.andreasgut.MuehleWebSpringBoot;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class GameController {

    @GetMapping(
            path = "/game/controller")
    public ModelAndView loadGameHTML() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/game.html");
        return modelAndView;}




    @PostMapping(
            path = "/game/controller")
    public void printBody(@RequestBody String body){
        System.out.println(body);
    }


}
