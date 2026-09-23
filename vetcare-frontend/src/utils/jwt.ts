export interface JwtPayload {
  sub?: string; // email
  roles?: string | string[];
  role?: string | string[];
  authorities?: string | string[];
  exp?: number;
}

export const parseJwt = (token: string): JwtPayload | null => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      window
        .atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(jsonPayload);
  } catch {
    return null;
  }
};

export const extractRolesFromToken = (token: string): string[] => {
  const payload = parseJwt(token);
  if (!payload) return [];

  const rawRoles = payload.roles || payload.role || payload.authorities;
  if (!rawRoles) return [];

  if (Array.isArray(rawRoles)) {
    return rawRoles;
  }
  return [rawRoles];
};