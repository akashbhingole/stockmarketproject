package com.conf.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.exception.UnAuthorizeException;
import com.netflix.zuul.context.RequestContext;

/**
 * This filter will check the validity of Google Token, and will try to refresh
 * it if it's not valid anymore.
 *
 * @author Glow Team
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	//@Autowired
	//private GoogleOauthService googleOauthService;

	private ConcurrentMap<String, Boolean> refreshMap = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			validateToken(request, response);
			filterChain.doFilter(request, response);
		} catch (UnAuthorizeException e) {
			logger.warn(e.getMessage());
			HttpSession session = request.getSession();
			if(session != null){
				unlock(session);
			}
			response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
		}
	}

	private void validateToken(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizeException {
		HttpSession session = request.getSession();
		TokenDetails tokenDetails = (TokenDetails) session.getAttribute("tokenDetails");
		if (tokenDetails == null) {
			throw new UnAuthorizeException("session doesn't exist");
		}
		if (tokenDetails.isExpired()) {
			if (!locked(session)) {
				lock(session);
				//String refreshToken = null;//googleOauthService.refreshToken(tokenDetails.getUsername());
				//tokenDetails = googleOauthService.validateToken(refreshToken);
				session.setAttribute("tokenDetails", tokenDetails);
				response.addHeader("refresh_token", tokenDetails.getToken());
				unlock(session);
			} else {
				for (int i = 0; locked(session) && i < 20; i++) {
					try {
						logger.info("waiting to refresh for " + tokenDetails.getUsername());
						Thread.sleep(100);
					} catch (InterruptedException e) {
						logger.error("sleep error ", e);
					}
				}
				tokenDetails = (TokenDetails) session.getAttribute("tokenDetails");
			}
		}
		RequestContext.getCurrentContext().getZuulRequestHeaders().put("token", tokenDetails.getToken());
	}

	private synchronized void lock(HttpSession session) {
		refreshMap.put(session.getId(), true);
	}

	private synchronized void unlock(HttpSession session) {
		refreshMap.remove(session.getId());
		logger.debug("map size " + refreshMap.size());
	}

	private synchronized boolean locked(HttpSession session) {
		return refreshMap.containsKey(session.getId());
	}
}
