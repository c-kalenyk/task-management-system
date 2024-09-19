package mate.academy.taskmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementsystem.dto.user.UserLoginRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(),
                        requestDto.getPassword())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        UserLoginResponseDto responseDto = new UserLoginResponseDto();
        responseDto.setToken(token);
        return responseDto;
    }
}
