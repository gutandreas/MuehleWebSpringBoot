package edu.andreasgut.MuehleWebSpringBoot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class GameController {

    @GetMapping(
            path = "/game.html")
    public ModelAndView loadGame(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/game.html");
        return modelAndView;}


    @PostMapping(
            path = "/game.html")
    public void printBody(@RequestBody String body){
        System.out.println(body);

    }


}
