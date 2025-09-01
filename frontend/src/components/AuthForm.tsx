import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { useToast } from '@/hooks/use-toast';
import api from '../api/api';
import { AxiosError } from 'axios';

interface AuthFormProps {
  type: 'login' | 'register';
  onSubmitSuccess: () => void;
}

const AuthForm = ({ type, onSubmitSuccess }: AuthFormProps) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: ''
  });

  const [isLoading, setIsLoading] = useState(false);
  const { toast } = useToast();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true); 

    if (type === 'register' && !formData.name.trim()) {
      toast({ title: "Nome é obrigatório", variant: "destructive" });
      setIsLoading(false);
      return;
    }
    if (!formData.email.trim() || !formData.password.trim()) {
      toast({ title: "Preencha todos os campos", variant: "destructive" });
      setIsLoading(false);
      return;
    }

    try {
      const endpoint = type === 'register' ? '/register' : '/login';
      
      const payload = type === 'register' 
        ? { name: formData.name, email: formData.email, password: formData.password }
        : { email: formData.email, password: formData.password };
      
      if(payload.password.length < 6 && type === 'register'){
          toast({
            title: `Erro ao criar conta`,
            description: 'A senha deve ter mais de 6 caracteres!',
            variant: "destructive"
          });
          return;
      }
      const response = await api.post(endpoint, payload);

      if(type !== 'register' && response.data.token){
        localStorage.setItem("token", response.data.token);
      }
      onSubmitSuccess();

    } catch (error) {
      const axiosError = error as AxiosError<{ message: string }>;
      let errorMessage = "Ocorreu um erro. Tente novamente."; 

      if (axiosError.response?.data) {
        if (typeof axiosError.response.data === 'object' && axiosError.response.data.message) {
          errorMessage = axiosError.response.data.message;
        } 
        else if (typeof axiosError.response.data === 'string') {
          errorMessage = axiosError.response.data;
        }
      }
      
      toast({
        title: `Erro ao ${type === 'register' ? 'criar conta' : 'fazer login'}`,
        description: errorMessage,
        variant: "destructive"
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (field: string, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4" style={{ background: 'var(--gradient-soft)' }}>
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="text-center">
          <CardTitle className="text-2xl font-bold">
            {type === 'register' ? 'Criar Conta' : 'Entrar'}
          </CardTitle>
          <CardDescription>
            {type === 'register' 
              ? 'Cadastre-se para monitorar os preços' 
              : 'Entre com suas credenciais'
            }
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            {type === 'register' && (
              <div className="space-y-2">
                <Label htmlFor="name">Nome</Label>
                <Input
                  id="name"
                  value={formData.name}
                  onChange={(e) => handleChange('name', e.target.value)}
                  placeholder="Seu nome completo"
                />
              </div>
            )}
            
            <div className="space-y-2">
              <Label htmlFor="email">E-mail</Label>
              <Input
                id="email"
                type="email"
                value={formData.email}
                onChange={(e) => handleChange('email', e.target.value)}
                placeholder="seu@email.com"
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="password">Senha</Label>
              <Input
                id="password"
                type="password"
                value={formData.password}
                onChange={(e) => handleChange('password', e.target.value)}
                placeholder="Sua senha"
              />
            </div>
            
            <Button type="submit" className="w-full">
              {type === 'register' ? 'Cadastrar' : 'Entrar'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default AuthForm;