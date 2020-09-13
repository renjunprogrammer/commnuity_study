package community_study.controller;

import community_study.dto.AccessTokenDTO;
import community_study.dto.GithubUserDTO;
import community_study.mapper.UserMapper;
import community_study.model.User;
import community_study.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    UserMapper userMapper;

    @Value("${accessToken.clientId}")
    private String clientId;

    @Value("${accessToken.clientSecret}")
    private String clientSecret;

    @Value("${accessToken.redirectUri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtUpdate(user.getGmtCreate());
            userMapper.inserUser(user);
            //登录成功 写cookie和session
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/index";
        }else {
            //登录失败，重新登录
            return "index";
        }
    }
}
