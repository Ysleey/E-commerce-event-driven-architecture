import { UserProfile, UserRole } from './user.model';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  username: UserProfile['username'];
  roles: UserRole[];
}

export interface AuthSession {
  token: string;
  username: UserProfile['username'];
  roles: UserProfile['roles'];
  expiresAt: number;
}
