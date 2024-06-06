package team2.elearningapplication.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team2.elearningapplication.security.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(
                "/api/v1/user/register",
                "/api/v1/user/login",
                "/api/v1/user/verify-otp",
                "/api/v1/user/forgotPassword",
                "/api/v1/user/changePassword",
                "/api/v1/user/send-otp-forgot-password",
                "/api/v1/user/verify-otp-forgotPass",
                "/api/v1/user/change-profile",
                "/api/v1/user/log-out",
                "/api/v1/user/total-user",
                "/api/v1/user/getOTP",
                "/api/v1/user/resendOTP",
                "/api/v1/user/get-user-by-username",
                "/api/v1/user/set-role-user",
                "/api/v1/user/get-all-user",
                "/api-docs",
                "/swagger-ui",


                "/api/v1/course/add-course",
                "/api/v1/course/update-course",
                "/api/v1/course/delete-course",
                "/api/v1/course/find-all-course",
                "/api/v1/course/get-top-course",
                "/api/v1/course/get-newest-course",
                "/api/v1/course/get-course-user",
                "/api/v1/course/get-total-course",
                "/api/v1/course/search-course",
                "/api/v1/course/get-course-by-id",
                "/api/v1/course/page-course",
                "/api/v1/course/enroll-course",
                "/api/v1/course/confirm-payment",
                "/api/v1/course/check-enroll",
                "/api/v1/course/total-revenue",
                "/api/v1/course/find-all-course-by-deleted",


                "/api/v1/category/get-category-by-id",
                "/api/v1/category/add-category",
                "/api/v1/category/update-category",
                "/api/v1/category/delete-category",
                "/api/v1/category/find-all-category",
                "/api/v1/category/find-all-category-by-deleted",


                "/api/v1/lesson/add-lesson",
                "/api/v1/lesson/update-lesson",
                "/api/v1/lesson/delete-lesson",
                "/api/v1/lesson/find-all-lesson",
                "/api/v1/lesson/get-lesson-by-id",
                "/api/v1/lesson/page-lesson",
                "/api/v1/lesson/get-lessons-by-course",
                "/api/v1/lesson/find-all-lesson-by-deleted",
                "/api/v1/lesson/complete-lesson",
                "/api/v1/lesson/get-tracking-course",


                "/api/v1/answer/add-answer",
                "/api/v1/answer/update-answer",
                "/api/v1/answer/delete-answer",
                "/api/v1/answer/find-all-answer",
                "/api/v1/answer/get-answer-by-id",
                "/api/v1/answer/find-all-answer-by-deleted",
                "/api/v1/answer/get-answer-by-question-id",

                "/api/v1/post/add-post",
                "/api/v1/post/update-post",
                "/api/v1/post/delete-post",
                "/api/v1/post/find-all-post",
                "/api/v1/post/get-post-by-id",

                "/api/v1/question/add-question",
                "/api/v1/question/update-question",
                "/api/v1/question/delete-question",
                "/api/v1/question/find-all-question",
                "/api/v1/question/get-question-by-id",
                "/api/v1/question/get-question-page",
                "/api/v1/question/find-all-question-by-deleted",
                "/api/v1/question/get-questions-by-quiz-id",

                "/api/v1/quiz/add-quiz",
                "/api/v1/quiz/update-quiz",
                "/api/v1/quiz/delete-quiz",
                "/api/v1/quiz/find-all-quiz",
                "/api/v1/quiz/get-quiz-by-id",
                "/api/v1/quiz/start-quiz",
                "/api/v1/quiz/next-question",
                "/api/v1/quiz/finish-quiz",
                "/api/v1/quiz/reset-quiz",
                "/api/v1/quiz/find-all-quiz-by-deleted",
                "/api/v1/quiz/get-all-session-quiz",
                "/api/v1/quiz/get-correct-answers-by-session",

                "/api/v1/payment/get-payment-user",
                "/api/v1/payment/get-all-payment",
                "/api/v1/payment/add-payment",
                "api/v1/payment/payment-info",
                "/swagger-ui/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
