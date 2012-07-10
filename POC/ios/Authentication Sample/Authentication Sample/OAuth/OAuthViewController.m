//
//  OAuthViewController.m
//  Authentication Sample
//
//  Created by Ryan Latta on 5/14/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "OAuthViewController.h"
#import "SBJson.h"


@implementation OAuthViewController
@synthesize web;
@synthesize code;

+(BOOL) isAuthenticated {
    NSString *token = [[NSUserDefaults standardUserDefaults] stringForKey:@"token"];
    if([token length] > 0) {
        return YES;
    }
    return NO;
}

+(NSString *) getToken {
    return [[NSUserDefaults standardUserDefaults] stringForKey:@"token"];
}
- (id) initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    return [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
}
//Where you programatically create your views.
- (void) loadView {
    CGRect frame = [[[[UIApplication sharedApplication] delegate] window] frame];
    self.web = [[[UIWebView alloc] initWithFrame:CGRectMake(frame.origin.x, frame.origin.y, frame.size.width, frame.size.height)] autorelease];
    self.view = web;
    self.web.delegate = self;
}

- (void) viewDidLoad {
    [self.web loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"http://local.slidev.org:8080/api/oauth/authorize?redirect_uri=https://localhost&client_id=EGbI4LaLaL"]]];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (void) dealloc {
    [web release];
    [code release];
    [super dealloc];
}

/**
 * Method that parses the OAuth token out of the response.
 */
- (void) getOauthToken {
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"http://local.slidev.org:8080/api/oauth/token?client_id=EGbI4LaLaL&client_secret=iGdeAGCugi4VwZNtMJR062nNKjB7gRKUjSB0AcZqpn8Beeee&code=%@&redirect_uri=NONCE", self.code]];
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
    [request startSynchronous];
    NSLog(@"Got a response of %@", [request responseString]);
    NSDictionary *token = [[request responseString] JSONValue];
    [[NSUserDefaults standardUserDefaults] setObject:[token objectForKey:@"access_token"] forKey:@"token"];
    [self.navigationController dismissModalViewControllerAnimated:YES];
}

/**
 * UIWebView Delegate
 */
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    return YES;
    
}

/**
 * Once the authorization code page loads this method will use the code to get the oauth token
 */
- (void) webViewDidFinishLoad:(UIWebView *)webView {
    NSString *requestURL = [[webView.request URL] relativeString];
    NSString *response = [webView stringByEvaluatingJavaScriptFromString:@"document.documentElement.textContent"];
    if([requestURL hasSuffix:@"saml/sso/post"])
    {
        NSLog(@"The auth code request is %@", response);
        self.code = [[response JSONValue] objectForKey:@"authorization_code"];
        [self getOauthToken];
        
    }
}
   
@end
