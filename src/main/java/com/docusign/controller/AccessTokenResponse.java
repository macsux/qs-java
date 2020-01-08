package com.docusign.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessTokenResponse
{
    String access_token;
    String token_type;
    int expires_in;
}
