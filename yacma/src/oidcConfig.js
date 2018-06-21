import { UserManager } from 'oidc-client'

export default () => {
  const port = window.location.port ? `:${window.location.port}` : ''
  const currentUrl = `${window.location.protocol}//${window.location
    .hostname}${port}`

  return new UserManager({
    authority: 'http://localhost:8080/openid-connect-server-webapp',
    client_id: 'app',
    redirect_uri: `${currentUrl}/redirect`,
    popup_redirect_uri: `${currentUrl}/redirect`,
    response_type: 'token id_token',
    scope: 'openid email profile',
    automaticSilentRenew: true,
    filterProtocolClaims: true,
    loadUserInfo: true
  })
}
