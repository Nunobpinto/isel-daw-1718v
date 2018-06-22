import { UserManager } from 'oidc-client'

export default () => {
  const port = window.location.port ? `:${window.location.port}` : ''
  const currentUrl = `${window.location.protocol}//${window.location
    .hostname}${port}`

  return new UserManager({
    authority: 'http://35.189.66.182/openid-connect-server-webapp',
    client_id: 'daw',
    redirect_uri: `${currentUrl}/redirect`,
    popup_redirect_uri: `${currentUrl}/redirect`,
    response_type: 'token id_token',
    scope: 'openid email profile',
    automaticSilentRenew: true,
    filterProtocolClaims: true,
    loadUserInfo: true
  })
}
