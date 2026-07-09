export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  username: string;
  roles: string[];
}

export interface AuthSession {
  token: string;
  username: string;
  roles: string[];
  expiresAt: number;
}
